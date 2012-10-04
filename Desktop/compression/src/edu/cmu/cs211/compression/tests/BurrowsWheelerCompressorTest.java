package edu.cmu.cs211.compression.tests;

import java.util.Random;

import org.junit.Test;

import edu.cmu.cs211.compression.bw.BurrowsWheelerCompressor;

public class BurrowsWheelerCompressorTest
{
	
	/* Note: This test checks the BurrowsWheelerCompressor, which uses
	 * your BurrowsWheelerTransformer, MoveToFrontTransformer, and HuffmanCode.
	 * You should test each of these classes independently first to make sure
	 * they all work on their own.
	 */ 
	
  @Test
  public void testRoundTrip() throws Exception
  {
    byte[] x = new byte[1000];
    new Random(42).nextBytes(x);
    TestUtil.checkRoundTrip(new BurrowsWheelerCompressor(), x);
  }
  @Test
  public void simple() throws Exception
  {
    String[] tests = new String[]
    { "asdfddffaassdasdfs", "asdfaaaaaaaadaaadaaaaaaafaaaaaaaaa",
        "The red rear rikes to grrrrrrrrrrr" };
    for (int i = 0; i < tests.length; i++)
    {
      byte[] input = tests[i].getBytes("ASCII");
      TestUtil.checkRoundTrip(new BurrowsWheelerCompressor(), input);
    }
  }
  
}