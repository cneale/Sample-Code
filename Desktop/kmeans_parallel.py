import csv
import math
import random
import getopt
import numpy
import sys
from mpi4py import MPI
import cProfile
comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

def usage():
    print '$> python kmeans_parallel.py <required args> \n' + \
        '\t-c <#> \t\tNumber of clusters to form\n' + \
        '\t-i <file> \tFilename for raw data input\n' + \
        '\t-o <file> \tFilename for data output\n' + \
        '\t-v <#> \t\tMinimum allowable variance\n'

def sub_group(n_elements,k_groups):
    num_members = int(math.ceil(float(len(n_elements))/float(k_groups)))
    subgroups = []
    st = 0
    end= 0
    num = 0
    for x in range(k_groups):
        subgroups.append([n_elements[y] for y in range(len(n_elements)) if y%k_groups == x])
    for x in range(len(subgroups)):
        for y in range(len(subgroups[x])):
            subgroups[x][y] = num
            num = num + 1
    return subgroups

def handleArgs(args):
    #set up initial values
    numClusters = -1
    input_ = None
    output = None
    variance = float('-inf')
    datapoints = []

    try:
        optlist, args = getopt.getopt(args[1:],'c:o:i:v:')
    except getopt.GetoptError, err:
        print str(err)
        usage()
        sys.exit(10)

    for key, val in optlist:
        if key == '-c':
            numClusters = int(val)
        elif key == '-o':
            output = val
        elif key == '-i':
            input_ = val
        elif key == '-v':
            variance = float(val)
    #check for valid args
    if numClusters < 0 or output is None or input_ is None or variance == float('-inf'):
        usage()
        sys.exit()
    return (numClusters,variance,input_,output)

def dist((x,y), (t,u)):
    return math.sqrt(math.pow((x - t),2) + \
                         math.pow((y - u),2))

def findInSubgroup(subgroups,n):
    for x in range(len(subgroups)):
        if n in subgroups[x]:
            return (x,subgroups[x].index(n),len(subgroups[x]))

    return (-1,-1)

#splits a list of clusters (a list of lists of points) into n subgroups of clusters
def divvyClusters(n,clusters):
    if n <= 1:
        return [clusters]
    elif len(clusters) < n: # TODO more processors than clusters..create sub clusters?
        return divvyClusters(len(clusters),clusters)
    else:
        divvyed = []
        for i in range(0,n):
            divvyed.append([clusters[x] for x in range(0,len(clusters)) if x%n == i])
        return divvyed

#used if more than one process is assigned to a cluster
#split up data (points or DNA strands) in a cluster
#returns n subclusters
def divvyData(n,cluster):
    if n <= 1:
        return [cluster]
    elif len(cluster) < n:
        return divvyData(len(cluster),cluster)
    else:
        divvyed = []
        for i in range(0,n):
            divvyed.append([cluster[x] for x in range(0,len(cluster)) if x%n == i])
        return divvyed

#compute the centroid point of a given cluster
def computeCentroid(cluster):
    (x,y) = reduce(lambda (x_1,y_1),(x_2,y_2) : ((x_1+x_2),(y_1+y_2)),cluster)
    return (x/float(len(cluster)),y/float(len(cluster)))

#compute the weighted centroid of a subcluster
def computeWeightedCentroid(subcluster):
    (x,y) = computeCentroid(subcluster)
    return (x*float(len(subcluster)),y*float(len(subcluster)))

def combineWeightedCentroids(subcentroids,n):
    (x,y) = reduce(lambda (x_1,y_1),(x_2,y_2) : ((x_1+x_2),(y_1+y_2)),subcentroids)
    return (x/float(n),y/(float(n)))

def kmeans_2D(points,k,minvar):
    #select k random centroids - TODO this part need only be done by rank 0
    centroids = random.sample(points,k)
    done = False
    while not done:
        clusters = [[] for c in centroids]
        for p in points:
            index = 0
            shortest = float('inf')
            for i in range(len(clusters)):
                distance = dist(p,centroids[i])
                if distance < shortest:
                    shortest = distance
                    index = i
            clusters[index].append(p)
        divvyed = None
        if rank == 0:
            divvyed = divvyClusters(size,clusters)
        divvyed = comm.bcast(divvyed,root=0)
        if(size <= k):
            assignedClusters = divvyed[rank]
            for x in range(len(assignedClusters)):
                assignedClusters[x] = computeCentroid(assignedClusters[x])
            gathered_centroids = comm.gather(assignedClusters,root=0)
            if rank == 0:
                new_centroids = []
                for i in range(len(gathered_centroids)):
                    for j in range(len(gathered_centroids[i])):
                        new_centroids.append(gathered_centroids[i][j])
                new_centroids = sorted(new_centroids)
                maxvar = float('-inf')
                for i in range(len(new_centroids)):
                    var = dist(centroids[i],new_centroids[i])
                    maxvar = max(maxvar,var)
                if maxvar <= minvar:
                    done = True
                else:
                    centroids = new_centroids[:]
            done = comm.bcast(done,root=0)
        else: #more processors than there are clusters
            num_procs = range(size)
            subgroup = sub_group(num_procs,k)
            index,sub_index,group_size = findInSubgroup(subgroup,rank)
            group_cluster_size = 0
            for x in range(len(divvyed[index])):
                group_cluster_size = group_cluster_size + len(divvyed[index][x])
            for x in range(k):
                divvyed[x] = divvyData(len(subgroup[x]),divvyed[x][0])
            weightedCentroid = computeWeightedCentroid(divvyed[index][sub_index])

            wCentroids = comm.allgather(weightedCentroid) #each process has access
            centroid = (float('-inf'),float('-inf'))
            if sub_index == 0: #compute centroid from the relevant weighted sub centroids
                if rank == 0: #first sublist
                    centroid = combineWeightedCentroids(wCentroids[index:group_size],group_cluster_size)
                else:
                    #find displacement
                    displacement = 0
                    for x in range(index):
                        displacement = displacement + len(subgroup[x])
                    centroid = combineWeightedCentroids(wCentroids[displacement:displacement+group_size],group_cluster_size)

            new_centroids = comm.gather(centroid,root=0)
            if rank == 0:
                new_centroids = [new_centroids[x] for x in range(len(new_centroids)) if new_centroids[x] != (float('-inf'),float('-inf'))]
                new_centroids = sorted(new_centroids)
                maxvar = float('-inf')
                for i in range(len(new_centroids)):
                    var = dist(centroids[i],new_centroids[i])
                    maxvar = max(maxvar,var)
                if maxvar <= minvar:
                    done = True
                else:
                    centroids = new_centroids[:]
        done = comm.bcast(done,root=0)
    return centroids

def main():
    k,var,inputName,outputName = handleArgs(sys.argv)
    points = []
    if rank == 0:
        inputfile = open(inputName,"rb")
        outputfile = open(outputName,"w")
        writer = csv.writer(outputfile)
        reader = csv.reader(inputfile)
        for row in reader:
            (x,y) = (float(row[0]),float(row[1]))
            points.append((x,y))
    points = comm.bcast(points,root=0)
    centroids = kmeans_2D(points,k,var)
    if rank == 0:
        for i,c in enumerate(centroids):
            writer.writerow(c)
        inputfile.close()
        outputfile.flush()
        outputfile.close()

if __name__ == "__main__":
    cProfile.run("main()")
