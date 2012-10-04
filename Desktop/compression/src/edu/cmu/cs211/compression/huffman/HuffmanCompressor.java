package edu.cmu.cs211.compression.huffman;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.cmu.cs211.compression.Compressor;
import edu.cmu.cs211.compression.io.BitReader;
import edu.cmu.cs211.compression.io.BitWriter;

/**
 * A compressor that uses Huffman encoding as a mapping.
 */
public class HuffmanCompressor extends Compressor {
	// @see Compressor#compress(io.BitReader, io.BitWriter)
	@Override
    public void compress(BitReader reader, BitWriter writer) throws IOException {
		int fileBytes = reader.length();
		HuffmanCode hc;

		if (fileBytes == 0)
			return;

		hc = calcHuffmanCode(reader);
		hc.writeHeader(writer);
		writer.writeInt(fileBytes);
		
		reader.reset();

		for (int i = 0; i < fileBytes; i++)
			hc.encode((byte) reader.readByte(), writer);

		writer.flush();
	}

	/**
	 * Calculates a Huffman code for a given set of bits
	 */
	public static HuffmanCode calcHuffmanCode(BitReader reader)
			throws IOException {
		int[] freqArray = new int[256];
		int fileBytes;
		fileBytes = reader.length();
		Map<Byte, Integer> freqMap = new HashMap<Byte, Integer>();

		for (int i = 0; i < fileBytes; i++)
			freqArray[reader.readByte() & 0xff]++;

		for (int i = 0; i < freqArray.length; i++) {
			if (freqArray[i] != 0) {
				freqMap.put((byte) i, freqArray[i]);
			}
		}

		return new HuffmanCode(freqMap);
	}

	// @see Compressor#expand(io.BitReader, io.BitWriter)
	@Override
    public void expand(BitReader reader, BitWriter writer) throws IOException {
		if (reader.length() == 0)
			return;
		
		HuffmanCode hc = new HuffmanCode(reader);
		int fileBytes = reader.readInt();

		for (int i = 0; i < fileBytes; i++) {
			byte val = hc.decode(reader);
			writer.writeByte(val);
		}

		writer.flush();
	}
}
