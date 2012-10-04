package edu.cmu.cs211.compression;

import java.io.File;

import edu.cmu.cs211.compression.huffman.HuffmanCompressor;
import edu.cmu.cs211.compression.bw.BurrowsWheelerCompressor;


/**
 * A utility to compress or decompress files.
 * <p>
 * <tt>java tez TYPE MODE input output</tt>
 * <p>
 * Where TYPE is one of:
 * <ul>
 * <li><b>h</b> - Huffman</li>
 * <li><b>b</b> - Burrows-Wheeler</li>
 * </ul>
 * MODE is one of c or x for compress or expand.
 * 
 * <p>An example is:  <tt>java tez b c foo bar</tt></p>
 */
public class tez {

	public static void main (String args[]) throws Exception
	{
		if (args.length != 4) {
			System.out.println ("java tez TYPE MODE input output");
			return;
		}

		Compressor comp = readCompressor (args [0]);

		boolean compress;
		if (args [1].equals ("c"))
			compress = true;
		else if (args [1].equals ("x"))
			compress = false;
		else
			throw new RuntimeException ("Invalid compression mode");

		// the java api for files is officially annoying
		if (new File (args [3]).isDirectory())
			args [3] = new File (args [3], new File (args [2]).getName()).getAbsolutePath();
		

		if (compress) {
			comp.compress (args [2], args [3]);
		}
		else {
			comp.expand (args [2], args [3]);
		}
	}

	static Compressor readCompressor (String arg)
	{
		if (arg.equals ("h"))
			return new HuffmanCompressor ();
		if (arg.equals ("b"))
			return new BurrowsWheelerCompressor ();
		else
			throw new RuntimeException ("Invalid compression mode");
	}
}
