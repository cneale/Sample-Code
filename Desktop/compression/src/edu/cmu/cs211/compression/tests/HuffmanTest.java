package edu.cmu.cs211.compression.tests;

import java.util.Random;

import org.junit.Test;

import edu.cmu.cs211.compression.huffman.HuffmanCompressor;

// This is a stub for you. Please write more tests!
public class HuffmanTest {

	@Test
	public void testRoundTrip () throws Exception
	{
		byte[] x = new byte [1000];
		new Random (42).nextBytes (x);
		TestUtil.checkRoundTrip (new HuffmanCompressor (), x);
	}
	
  @Test
  public void simple() throws Exception {
    String[] tests = new String[] { "asdfddffaassdasdfs",
                                "asdfaaaaaaaadaaadaaaaaaafaaaaaaaaa",
                                "The red rear rikes to grrrrrrrrrrr" };
    for (int i = 0; i < tests.length; i++) {
      byte[] input = tests[i].getBytes("ASCII");
      TestUtil.checkRoundTrip(new HuffmanCompressor (),input);
    }
  }
}
