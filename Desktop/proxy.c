#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "csapp.h"

typedef struct Node{
  int numBytes;
  char uri[MAXLINE];
  char *html;
  struct Node *prev;
  struct Node *next;

}cacheEntry;

#define MAX_OBJECT_SIZE (1 << 10)*100

void serverSide(int fd);
void clientSide(rio_t *rioS, int serverfd, rio_t *rioC, int clientfd, char *uri);
void parseUri(char *uri, char **domain, char **fileName, int *port);
int parseHeaders(rio_t *server, int serverfd, rio_t *client, int clientfd, char *domain);
void clienterror(int fd, char *cause, char *errnum, char *shortmsg, char *longmsg);
struct hostent *gethostbyname_ts(char *hostname);
struct hostent *gethostbyaddr_ts(const char *addr, int len, int type);
int open_clientfd_ts(char *hostname, int portno);
int Open_clientfd_ts(char *hostname, int portno);
void *thread(void *connfd);
cacheEntry *removeCacheEntry(cacheEntry *CE);
void insert(cacheEntry *CE);
cacheEntry *searchCache(char *uri);
cacheEntry *head, *tail;
int numObjects;
static sem_t mutex;
static sem_t rwlock;



int main (int argc, char *argv [])

{
  int port,listenfd,*connfd,clientlen;
   struct sockaddr_in clientaddr;
   pthread_t tid;
   numObjects = 0;
   Sem_init(&mutex,0,1);
   Sem_init(&rwlock,0,1);
   Signal(SIGPIPE,SIG_IGN);
   printf("int...%d\n",MAX_OBJECT_SIZE);
   if(argc != 2){
       fprintf(stderr, "usage: %s <port>\n", argv[0]);
       exit(1);
   }
   port = atoi(argv[1]);
   listenfd = Open_listenfd(port);

   while(1){
       clientlen = sizeof(clientaddr);
       connfd = (int*)Malloc(sizeof(int));
       *connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);
       Pthread_create(&tid, NULL, thread, connfd);
       Pthread_detach(tid);
   }
}
/*
 * *thread - function called with a new thread is created
 */
void *thread(void *vargp){
  int connfd = *((int *)vargp);
  Free(vargp);
  serverSide(connfd);
  return NULL;
}

/*
*simulates an intermediary server to a browser
*/
void serverSide(int fd){
   int clientfd,port;
   struct hostent *hp;
   char buf[MAXLINE], method[MAXLINE], uri[MAXLINE], version[MAXLINE],uri2[MAXLINE];
   char *domain, *fileName;
   rio_t rioS, rioC;
   cacheEntry *temp;
   domain = malloc(MAXLINE);
   fileName = malloc(MAXLINE);

   Rio_readinitb(&rioS, fd);
   if(rio_readlineb(&rioS, buf, MAXLINE) < 0){
       close(fd);
       return;
   }

   sscanf(buf, "%s %s %s", method, uri, version);
   printf("Method: %s, URI: %s, Version: %s\n",method,uri,version);
   strcpy(uri2,uri);

  if(strcasecmp(method,"GET")){
       clienterror(fd,method, "501", "Not implemented",
                                   "Only GET requests are allowed");
       return;
   }

  parseUri(uri, &domain, &fileName, &port);

  if((temp = searchCache(uri2)) != NULL){
    rio_writen(fd,temp->html,temp->numBytes);
    close(fd);
    P(&rwlock);
    removeCacheEntry(temp);
    insert(temp);
    V(&rwlock);
    return;
  }
   
  hp = gethostbyname_ts(domain);
  clientfd = Open_clientfd_ts(domain, port);
  
  if(rio_readinitb(&rioC, clientfd) < 0){
    close(clientfd);
    return;
  }
  
  sprintf(buf, "%s %s %s\r\n", method, fileName, "HTTP/1.0");
  if(rio_writen(clientfd, buf, strlen(buf)) < 0){
    close(clientfd);
    close(fd);
    return;
  }
  
  sprintf(buf, "Host: %s\r\n", domain);
  if(rio_writen(clientfd, buf, strlen(buf)) < 0){
    close(clientfd);
    close(fd);
    return;
    }
  
  if(parseHeaders(&rioS, fd, &rioC, clientfd, domain) < 0){
    printf("error parsing\n");
    close(fd);
    close(clientfd);
    return;
  }

  clientSide(&rioS, fd, &rioC, clientfd,uri2);
  close(fd);
}

void clientSide(rio_t *rioS, int serverfd, rio_t *rioC, int clientfd, char *uri){
  char buf[MAXLINE];
  char *cachebuf = Malloc(MAX_OBJECT_SIZE*sizeof(char));
  int bytes;
  int totalBytes = 0;
  cacheEntry *tmp;
  while((bytes = rio_readnb(rioC,buf,MAXLINE)) > 0){
    rio_writen(serverfd, buf, bytes);
    if(totalBytes + bytes <= MAX_OBJECT_SIZE){
       memcpy(cachebuf+totalBytes,buf,bytes);
    }
      printf("Pointer %ld\n",(long int)cachebuf+totalBytes);
    totalBytes += bytes;
  }
  if(numObjects > 10){
    printf("numObjects\n");
    exit(-2);
  }
  if(numObjects == 10){
    tmp = head;
    while(tmp->next != NULL){
      tmp = tmp->next;
    }
    P(&rwlock);
    Free(removeCacheEntry(tmp));
    V(&rwlock);
  }
  if(totalBytes <= MAX_OBJECT_SIZE){
    tmp = Malloc(sizeof(cacheEntry));
    tmp->numBytes = totalBytes;
    tmp->html = cachebuf;
    strcpy(tmp->uri,uri);
    P(&rwlock);
    insert(tmp);
    V(&rwlock);
  }else{
    Free(cachebuf);
  }
   close(clientfd);
}
/*
 * parseUri - tokenizes the uri setting the domain, fileName, and port
 */
void parseUri(char *uri, char **domain, char **fileName, int *port){
   char *ptr;
   strtok(uri, "/");
   *domain = strtok(NULL, "/");

   ptr = strtok(NULL, "");
   if(ptr !=NULL){
     sprintf(*fileName, "/%s",ptr);
   }else{
     *fileName = "/";
   }


    if((ptr = strstr(*domain,":")) == NULL){
       *port = 80;
   }else{
      *port = atoi(ptr+1);
      *domain = strtok(*domain, ":");
   }
}
/*
 * parseHeaders - Intercepts headers sent from browser and removes/replaces certains headers
 * as per the writeup
 */
int parseHeaders(rio_t *server, int serverfd, rio_t *client, int clientfd, char *domain){
 int svr;
 char serverbuf[MAXLINE], clientbuf[MAXLINE], hostheader[MAXLINE];

 
   svr = rio_readlineb(server,serverbuf,MAXLINE);
   sprintf(hostheader, "Host: %s\r\n",domain);

   while(strcmp(serverbuf,"\r\n") && svr > 0){
     if(strncasecmp(serverbuf, "Connection:", 11) == 0 ||
	strncasecmp(serverbuf, "Keep-Alive:", 11) == 0){
       strcpy(clientbuf, "Connection: close\r\n");
       if(rio_writen(clientfd, clientbuf, strlen(clientbuf)) < 0){
         return -1;
       }
       printf(clientbuf);
     }else if(strncasecmp(serverbuf, "Proxy-Connection:", 17) == 0){
       strcpy(clientbuf, "Proxy-Connection: close\r\n");
       if(rio_writen(clientfd,clientbuf, strlen(clientbuf)) < 0){
         return -1;
       }
       printf(clientbuf);
     }else if(strcmp(hostheader,serverbuf)){
       sprintf(clientbuf,"%s",serverbuf);
       if(rio_writen(clientfd,clientbuf, svr) < 0){
         return -1;
       }
     }
     svr = rio_readlineb(server,serverbuf,MAXLINE);
   }
   
   if(svr >= 0){
     strcpy(clientbuf, "\r\n");
     rio_writen(clientfd, clientbuf, strlen(clientbuf));
   }

   return svr;
}
/*
 * client error - helper function that sends an html body to the browser if
 * the method is specifically "GET"
 */
void clienterror(int fd, char *cause, char *errnum,
                char *shortmsg, char *longmsg)
{
   char buf[MAXLINE], body[MAXBUF];

   /* Build the HTTP response body */
   sprintf(body, "<html><title>Tiny Error</title>");
   sprintf(body, "%s<body bgcolor=""ffffff"">\r\n", body);
   sprintf(body, "%s%s: %s\r\n", body, errnum, shortmsg);
   sprintf(body, "%s<p>%s: %s\r\n", body, longmsg, cause);
   sprintf(body, "%s<hr><em>The Tiny Web server</em>\r\n", body);

   /* Print the HTTP response */
   sprintf(buf, "HTTP/1.0 %s %s\r\n", errnum, shortmsg);
   Rio_writen(fd, buf, strlen(buf));
   sprintf(buf, "Content-type: text/html\r\n");
   Rio_writen(fd, buf, strlen(buf));
   sprintf(buf, "Content-length: %d\r\n\r\n", (int)strlen(body));
   Rio_writen(fd, buf, strlen(buf));
   Rio_writen(fd, body, strlen(body));
}
/*
 * *gethostbyname_ts - thread safe implementation of gethostbyname
 */
struct hostent *gethostbyname_ts(char *hostname){
  struct hostent *sharedp, *unsharedp;

  unsharedp = Malloc(sizeof(struct hostent));
  P(&mutex);
  sharedp = gethostbyname(hostname);
  *unsharedp = *sharedp;
  V(&mutex);
  return unsharedp;
}
/*
 * *gethostbyaddr_ts - thread safe implementation of gethostbyaddr
 */
struct hostent *gethostbyaddr_ts(const char *addr, int len, int type){
  struct hostent *sharedp, *unsharedp;

  unsharedp = Malloc(sizeof(struct hostent));
  P(&mutex);
  sharedp = gethostbyaddr(addr,len,type);
  *unsharedp = *sharedp;
  V(&mutex);
  return unsharedp;
}

/*
 * openclientfd_ts - thread safe implementation of openclientfd
 */
int open_clientfd_ts(char *hostname, int port) {
    int clientfd;
    struct hostent *hp;
    struct sockaddr_in serveraddr;

    if ((clientfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
        return -1; /* check errno for cause of error */

    /* Fill in the server's IP address and port */
    if ((hp = gethostbyname_ts(hostname)) == NULL)
        return -2; /* check h_errno for cause of error */
    bzero((char *) &serveraddr, sizeof(serveraddr));
    serveraddr.sin_family = AF_INET;
    bcopy((char *)hp->h_addr_list[0], 
          (char *)&serveraddr.sin_addr.s_addr, hp->h_length);
    serveraddr.sin_port = htons(port);

    /* Establish a connection with the server */
    if (connect(clientfd, (SA *) &serveraddr, sizeof(serveraddr)) < 0)
        return -1;

    free(hp);
    return clientfd;
}

/*
 * Open_clientfd_ts - wrapper function for openclientfd_ts
 */
int Open_clientfd_ts(char *hostname, int port){
  int rc;
  
  if((rc=open_clientfd_ts(hostname,port))<0) {
    if (rc==-1)
      unix_error("Open_clientfd_ts Unix error");
    else
      dns_error("Open_clientfd_ts DNS error");
  }
  return rc;
}
/*
 * insert - inserts the given cacheEntry into the cache linked list as head, 
 * fixes any pointers that need to be adjusted (i.e. previous head's prev ptr now points
 * to the  given cacheEntry as oppose to NULL
 */
void insert(cacheEntry *CE){
  if(head == NULL){
    head = CE;
    CE->prev = NULL;
    CE->next = NULL;
  }else{
    head->prev = CE;
    CE->next = head;
    head = CE;
    CE->prev = NULL;
  }
  numObjects++;
}
/*
 * searchCache - searches the cache for the html code corresponding to the given uri
 */
cacheEntry *searchCache(char *uri){
  cacheEntry *tmp = head;

  while(tmp != NULL){
    if(!strcmp(uri,tmp->uri)){
      return tmp;
    }
    tmp = tmp->next;
  }
  return NULL;
}
/*
 * removeCacheEntry - removes the given cache entry from the list and adjusts
 * all pointer pointing to it. returns the pointer to the newly removed cacheEntry
 */
cacheEntry *removeCacheEntry(cacheEntry *CE){
  if(head == CE){
    if(CE->next == NULL){
      Free(CE);
      head = NULL;
    }else{
      CE->next->prev = NULL;
      head = CE->next;
    }
  }else{
    CE->prev->next = CE->next;
    if(CE->next != NULL){
      CE->next->prev = CE->prev;
    }
  }
  numObjects--;
  return CE;
}
