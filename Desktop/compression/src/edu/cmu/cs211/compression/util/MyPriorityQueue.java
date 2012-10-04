package edu.cmu.cs211.compression.util;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.AbstractQueue;

/**
 * An unbounded priority {@linkplain Queue queue}. This queue orders elements
 * according to an order specified at construction time, which is specified
 * either according to their <i>natural order</i> (see {@link Comparable}), or
 * according to a {@link java.util.Comparator}, depending on which constructor
 * is used. A priority queue does not permit <tt>null</tt> elements. A
 * priority queue relying on natural ordering also does not permit insertion of
 * non-comparable objects (doing so may result in <tt>ClassCastException</tt>).
 * <p>
 * The <em>head</em> of this queue is the <em>least</em> element with
 * respect to the specified ordering. If multiple elements are tied for least
 * value, the head is one of those elements -- ties are broken arbitrarily. The
 * queue retrieval operations <tt>poll</tt>, <tt>remove</tt>,
 * <tt>peek</tt>, and <tt>element</tt> access the element at the head of
 * the queue.
 * <p>
 * A priority queue is unbounded, but has an internal <i>capacity</i> governing
 * the size of an array used to store the elements on the queue. It is always at
 * least as large as the queue size. As elements are added to a priority queue,
 * its capacity grows automatically. The details of the growth policy are not
 * specified.
 * <p>
 * This class and its iterator implement all of the <em>optional</em> methods
 * of the {@link Collection} and {@link Iterator} interfaces. The Iterator
 * provided in method {@link #iterator()} is <em>not</em> guaranteed to
 * traverse the elements of the MyPriorityQueue in any particular order. If you
 * need ordered traversal, consider using <tt>Arrays.sort(pq.toArray())</tt>.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> Multiple
 * threads should not access a <tt>MyPriorityQueue</tt> instance concurrently if
 * any of the threads modifies the list structurally. Instead, use the
 * thread-safe {@link java.util.concurrent.PriorityBlockingQueue} class.
 * <p>
 * Implementation note: this implementation provides O(log(n)) time for the
 * insertion methods (<tt>offer</tt>, <tt>poll</tt>, <tt>remove()</tt>
 * and <tt>add</tt>) methods; linear time for the <tt>remove(Object)</tt>
 * and <tt>contains(Object)</tt> methods; and constant time for the retrieval
 * methods (<tt>peek</tt>, <tt>element</tt>, and <tt>size</tt>).
 */
public class MyPriorityQueue<T> extends AbstractQueue<T> {

	private static final int DEFAULT_INITIAL_CAPACITY = 11;
	public T[] pq;
	private int size;
	private Comparator<T> qComparator;
	/**
	 * Creates a <tt>MyPriorityQueue</tt> with the default initial capacity (11)
	 * that orders its elements according to their natural ordering (using
	 * <tt>Comparable</tt>).
	 */
	public MyPriorityQueue ()
	{
		pq = (T[])new Object[DEFAULT_INITIAL_CAPACITY+1];
		size = 0;
		qComparator = null;
	}

	/**
	 * Creates a <tt>MyPriorityQueue</tt> with the specified initial capacity
	 * that orders its elements according to their natural ordering (using
	 * <tt>Comparable</tt>).
	 * 
	 * @param initialCapacity
	 *            the initial capacity for this priority queue.
	 * @throws IllegalArgumentException
	 *             if <tt>initialCapacity</tt> is less than 1
	 */
	public MyPriorityQueue (int initialCapacity)
	{
		if(initialCapacity < 1) throw new IllegalArgumentException("invalid capacity");
		pq = (T[])new Object[initialCapacity+1];
		size = 0;
		qComparator = null;
	}

	/**
	 * Creates a <tt>MyPriorityQueue</tt> with the specified initial capacity
	 * that orders its elements according to the specified comparator.
	 * 
	 * @param initialCapacity
	 *            the initial capacity for this priority queue.
	 * @param comparator
	 *            the comparator used to order this priority queue. If
	 *            <tt>null</tt> then the order depends on the elements'
	 *            natural ordering.
	 * @throws IllegalArgumentException
	 *             if <tt>initialCapacity</tt> is less than 1
	 */
	public MyPriorityQueue (int initialCapacity, Comparator<? super T> comparator)
	{
		pq = (T[])new Object[initialCapacity+1];
		size = 0;
		qComparator = (Comparator<T>) comparator;
	}
	
	/**
	 * Creates a <tt>MyPriorityQueue</tt> that orders its elements according to the specified comparator.
	 * 
	 * @param comparator
	 *            the comparator used to order this priority queue. If
	 *            <tt>null</tt> then the order depends on the elements'
	 *            natural ordering.
	 */
	public MyPriorityQueue (Comparator<? super T> comparator)
	{
		pq = (T[])new Object[DEFAULT_INITIAL_CAPACITY+1];
		size = 0;
		qComparator = (Comparator<T>) comparator;
	}


	/**
	 * Creates a <tt>MyPriorityQueue</tt> containing the elements in the
	 * specified collection. The priority queue has an initial capacity of 110%
	 * of the size of the specified collection or 1 if the collection is empty.
	 * If the specified collection is an instance of a
	 * {@link java.util.SortedSet} or is another <tt>MyPriorityQueue</tt>, the
	 * priority queue will be sorted according to the same comparator, or
	 * according to its elements' natural order if the collection is sorted
	 * according to its elements' natural order. Otherwise, the priority queue
	 * is ordered according to its elements' natural order.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this
	 *            priority queue.
	 * @throws ClassCastException
	 *             if elements of the specified collection cannot be compared to
	 *             one another according to the priority queue's ordering.
	 * @throws NullPointerException
	 *             if <tt>c</tt> or any element within it is <tt>null</tt>
	 */
	public MyPriorityQueue (Collection<? extends T> c)
	{
		if(c == null) throw new NullPointerException();
		int initialCapacity, i=0;
		
		//if c is a MyPriortiyQueue
		if(c instanceof MyPriorityQueue<?>){
			if(((MyPriorityQueue<T>)c).size() == 0){
				initialCapacity = 1;
			}else{
				initialCapacity = (int) (1.1*c.size());
			}
			pq = (T[])new Object[initialCapacity];
			size = c.size();
			qComparator = (Comparator<T>) ((MyPriorityQueue<?>)c).comparator();
			//MyPriorityQueue already contains a sorted set of elements
			while(((MyPriorityQueue<T>)c).size() != 0){
				T temp = ((MyPriorityQueue<T>)c).poll();
				if(temp == null)throw new NullPointerException("element in collection was null");
				pq[++i] = ((MyPriorityQueue<T>)c).poll();
			}
		//if c is a sorted set
		}else if(c instanceof java.util.SortedSet<?>){
			if(((java.util.SortedSet<T>)c).isEmpty()){
				initialCapacity = 1;
			}else{
				initialCapacity = (int)(1.1*c.size());
			}
			pq = (T[])new Object[initialCapacity+1];
			size = c.size();
			qComparator = (Comparator<T>) ((java.util.SortedSet<?>)c).comparator();

			while(!((java.util.SortedSet<T>)c).isEmpty()){
				T t = ((java.util.SortedSet<T>)c).first();
				if(t == null) throw new NullPointerException("element in collection was null");
				pq[++i] = t;
				c.remove(t);
			}
		}
		else{
			qComparator = null;
			if(c.size()==0){
				pq = (T[])new Object[2];
				size = 0;				
			}else{
				initialCapacity = c.size();
				pq = (T[])new Object[initialCapacity+1];
				size=initialCapacity;
				T[] temp = c.toArray((T[])new Object[c.size()]);
				 
				while(c.size() != 0){
					offer(temp[i++]);
				}
			}
			
		}
	}

	/**
	 * Returns the comparator used to order this collection, or <tt>null</tt>
	 * if this collection is sorted according to its elements natural ordering
	 * (using <tt>Comparable</tt>).
	 * 
	 * @return the comparator used to order this collection, or <tt>null</tt>
	 *         if this collection is sorted according to its elements natural
	 *         ordering.
	 */
	public Comparator<? super T> comparator ()
	{
		return qComparator;
	}
	

	// See superclass docs
	@Override
    public int size ()
	{
		return size;
	}

	/**
	 * Inserts the specified element into this priority queue.
	 * 
	 * @return <tt>true</tt>
	 * @throws ClassCastException
	 *             if the specified element cannot be compared with elements
	 *             currently in the priority queue according to the priority
	 *             queue's ordering.
	 * @throws NullPointerException
	 *             if the specified element is <tt>null</tt>.
	 */
	public boolean offer (T o)
	{
		
		if(o == null){
			throw new NullPointerException("null element was inserted into the Priority Queue");
		}
			//double capacity
		if(size+1 == pq.length){
			T[] temp = (T[])new Object[2*pq.length];
			for(int i = 1; i < pq.length; i++) temp[i] = pq[i]; 
			pq = temp;
		}
			
			int hole = ++size;
		
			if(qComparator == null){
				for(;hole>1 && ((Comparable) o).compareTo(pq[hole/2])<0; hole/=2) pq[hole] = pq[hole/2];
				pq[hole] = o;
				return true;
			}else{
				for(;hole>1 && qComparator.compare(o, pq[hole/2])<0;hole/=2) pq[hole] = pq[hole/2];
				pq[hole] = o;
				return true;
			}
		}
	

	// See superclass docs
	public T poll ()
	{
		if(size==0) return null;
		T min = pq[1];
		pq[1] = pq[size--];
		percolateDown(1);
		return min;
	}
	
	private void percolateDown(int hole){
		int child;
		T tmp = pq[hole];
		if(qComparator == null){
			for(; hole*2 <= size; hole = child){
				child = hole*2;
				if(child != size && ((Comparable) pq[child+1]).compareTo(pq[child]) < 0)
					child++;
			
				if(((Comparable) pq[child]).compareTo(tmp) < 0)
					pq[hole] = pq[child];
				else break;
			}
			pq[hole] = tmp;
		}else{
			for(; hole*2 <= size; hole = child){
				child = hole*2;
				if(child != size && qComparator.compare(pq[child+1],pq[child]) < 0)
					child++;
			
				if(qComparator.compare(pq[child],tmp) < 0)
					pq[hole] = pq[child];
				else break;
			}
			pq[hole] = tmp;
		}
	}

	// See superclass docs
	public T peek ()
	{
		return pq[1];
	}

	/**
	 * Returns an iterator over the elements in this queue. The iterator does
	 * not return the elements in any particular order. The iterator does not
	 * support the remove operation.
	 * 
	 * @return an iterator over the elements in this queue.
	 */
	@Override
    public Iterator<T> iterator ()
	{
		return new PQIterator();
	}	
	
	private class PQIterator implements Iterator<T>{

		private int index = 0;
		public boolean hasNext() {
			return index != size;
		}

		public T next() {
			if(index >= size) throw new NoSuchElementException();
			return pq[++index];
		}

		public void remove() {}
		
	}
	
	public void printArray(){
		System.out.println(pq);
	}
}
