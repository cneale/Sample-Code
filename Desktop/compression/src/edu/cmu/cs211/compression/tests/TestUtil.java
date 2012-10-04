package edu.cmu.cs211.compression.tests;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import edu.cmu.cs211.compression.Compressor;

/**
 * A utility class to help you write tests
 */
public class TestUtil {

	/**
	 * Checks that when compressed is expanded using comp, it is orig
	 */
	public static void checkDecode (Compressor comp, byte [] compressed, byte [] orig) throws Exception
	{
		byte [] decomp = comp.expand (compressed);
		assertTrue ("Decoding check failed", Arrays.equals (orig, decomp));
	}

	/**
	 * Checks that bytes round trips using comp
	 */
	public static void checkRoundTrip (Compressor comp, byte [] bytes) throws Exception
	{
		checkDecode (comp, comp.compress (bytes), bytes);
	}

}
