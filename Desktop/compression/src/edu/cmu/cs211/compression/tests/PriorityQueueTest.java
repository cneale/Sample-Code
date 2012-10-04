package edu.cmu.cs211.compression.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.junit.Test;

import edu.cmu.cs211.compression.util.MyPriorityQueue;

/*
 * Some of the provided tests were left for you to finish implementing.
 */
public class PriorityQueueTest
{
  /** A simple test that offers one element into the pq */
  @Test
  public void testOffer()
  {
    Queue<String> pq = new MyPriorityQueue<String>();
    boolean ret = pq.offer("blah");
    assertEquals("offer on queue returns true", true, ret);
    assertEquals("size on queue returns 1 after insert", 1, pq.size());
  }

  /** PQ does not allow a negative initial capacity */
  @Test(expected = IllegalArgumentException.class)
  public void capacityIllegalNeg()
  {
    new MyPriorityQueue<Object>(-2);
  }

  @Test(expected = NullPointerException.class)
  public void nullTest()
  {
	  Queue<String> q = new MyPriorityQueue<String>();
	  q.offer(null);
	  fail();
  }
  
  @Test
  public void stressTest()
  {
    PriorityQueue<Integer>   java = new PriorityQueue<Integer>();
    MyPriorityQueue<Integer> mine = new MyPriorityQueue<Integer>();
    
    Random r      = new Random();
    int    NITERS = 10000;

    for( int i = 0; i < NITERS; ++i )
    {
      //Create an element to potentially add to the PQ
      Integer mightadd = Integer.valueOf(r.nextInt(100));
      
      //TODO: randomly select an operation that mutates the queue
      // we want to be sure that the queue grows, so we add with
      // higher probability than removing
      if( r.nextFloat() < 0.66f )
      {
    	mine.offer(mightadd);
        java.offer(mightadd);
      }
      else
      {
    	mine.poll();
        java.poll();
      }
      
      //Make sure the state is still consistent
      assertPQequal(mine, java);
    }
  }
  
  /**
   * It is often helpful when you are unit testing a data strcture
   * to write helper methods that make sure that the state of your
   * data structure are consistent at any given point in time.
   * 
   * This method takes two priority queues and compares them for equality.
   */
  private <E> void assertPQequal(MyPriorityQueue<E> actual, PriorityQueue<E> expected)
  {
    //TODO: What else could we test here without modifying the PQ?
    

    //Array list uses the iterator of PQ to get all the elements
    // so this tests that your iterator returns all of the elements
    // in the PQ
    List<E> actual_elems = new ArrayList<E>(actual);
    List<E> expected_elems = new ArrayList<E>(expected);
    
    //Because the order of elements returned by the iterator is unspecified,
    //we restore a canonical ordering for the elements by sorting them.
    Collections.sort(actual_elems, actual.comparator());
    Collections.sort(expected_elems, expected.comparator());
    assertEquals(actual_elems, expected_elems);
  }
}
