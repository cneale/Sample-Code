package edu.cmu.cs211.compression.huffman;

import java.io.IOException;
import java.util.*;
import edu.cmu.cs211.compression.io.BitReader;
import edu.cmu.cs211.compression.io.BitWriter;
import edu.cmu.cs211.compression.util.MyPriorityQueue;

/**
 * Represents the Huffman code. The class supports building the Huffman tree
 * based either on the frequency of given symbols or by reading the tree from an
 * on-disk format. It also supports emitting the code word for a given symbol or
 * reading a symbol based on the code word. Lastly, it is able to write an
 * on-disk representation of the Huffman tree.  For testing purposes, we can also
 * create a Huffman code with a given HuffmanNode as the root.
 */
public class HuffmanCode {

	/** Code bit for a leaf node in file-based tree representation */
	private static final int LEAF = 0;
	/** Code bit for a parent node in file-based tree representation */
	private static final int PARENT = 1;
	/** Code bit for the left child in the file-based tree representation */
	private static final int LEFT = 0;
	/** Code bit for the right child in the file-based tree representation */
	private static final int RIGHT = 1;
	private HashMap<Byte,String> symbolList;
	private MyPriorityQueue<HuffmanNode> pq;
	private HuffmanNode tree;
	/**
	 * Creates a HuffmanCode given a Huffman tree.
	 * 
	 * @throws NullPointerException
	 *             if root is null
	 */
	public HuffmanCode (HuffmanNode root)
	{
		if(root == null) throw new NullPointerException();
		tree=root;
		symbolList = new HashMap<Byte,String>();
		String sequence = "";
		buildMap(root,sequence);
		pq=null;
	}
	//build a hashmap containing all the bytes, and their codes represented as a string
	private void buildMap(HuffmanNode t, String s){
		HuffmanNode temp = t;
		String right, left;
		if(temp.getRight()!=null){
			right=s.concat(Integer.toString(RIGHT));
			buildMap(temp.getRight(),right);
		}
		if(temp.getLeft()!=null){
			left=s.concat(Integer.toString(LEFT));
			buildMap(temp.getLeft(),left);
		}
		if(temp.isLeaf()){
			symbolList.put(new Byte(temp.getValue()),s);
		}
	}
	

	/**
	 * <p>
	 * Reads the Huffman header in from br deserialize the data at
	 * the leafs of the tree with br.readByte(). The data format for this header is defined
	 * by <tt>writeHeader</tt>
	 * </p>
	 *
	 * <p>
	 * Note that this is not used to read in a file you want to compress, but is
	 * used to read-in the Huffman codes from the header of an already compressed file.
	 * </p>
	 * @throws IOException
	 *             If there is a problem reading from the bit reader, if the
	 *             file ends before the full header can be read, or if the
	 *             header is not valid.
	 */
	public HuffmanCode (BitReader br) throws IOException
	{
		symbolList = new HashMap<Byte,String>();
		String sequence = "";
		tree = readHeader(br);
		buildMap(tree,sequence);
		pq = null;
	}
	//construct tree from header
	private HuffmanNode readHeader(BitReader br) throws IOException
	{
		HuffmanNode left, right;
		if(br.readBit()==LEAF){
			return (new HuffmanNode((byte)br.readByte()));
		}else{
			left = readHeader(br);
			right = readHeader(br);
			return (new HuffmanNode(left,right));
		}
	}

	/**
	 * Takes a list of (Byte, Frequency) pairs (here represented as a map)
	 * and builds a tree for encoding the data items using the Huffman
	 * algorithm.
	 * 
	 * @throws NullPointerException
	 *             If freqs is null
	 * @throws IllegalArgumentException
	 *             if freqs is empty
	 */
	public HuffmanCode (Map<Byte, Integer> freqs)
	{
		Byte temp;
		symbolList = new HashMap<Byte,String>();
		String sequence = "";
		pq = new MyPriorityQueue<HuffmanNode>(HComparator());
		if(freqs == null) throw new NullPointerException();
		if(freqs.isEmpty()) throw new IllegalArgumentException();
		Iterator<Map.Entry<Byte, Integer>> it = freqs.entrySet().iterator();
		while(it.hasNext()){
			temp = it.next().getKey();
			pq.offer(new HuffmanNode(freqs.get(temp),temp));
		}
		tree = buildFromQueue(pq);
		buildMap(tree,sequence);
	}
	
	private HuffmanNode buildFromQueue(MyPriorityQueue<HuffmanNode> p)
	{
		HuffmanNode temp1, temp2;
		while(pq.size() != 1){
			temp1 = pq.poll();
			temp2 = pq.poll();
			pq.add(new HuffmanNode(temp1,temp2));
		}
		return pq.poll();
	}

	/**
	 * <p>
	 * Turns this Huffman code into a stream of bits suitable
	 * for including in a compressed file.
	 * </p>
	 *
	 * <p>
	 * The format for the tree is defined recursively. To emit
	 * the entire tree, you start by emitting the root. When you emit
	 * a node, if the node is a leaf node, you write the bit <tt>LEAF</tt>
	 * and then call the <tt>writeByte</tt> method of <tt>BitWriter</tt> on
	 * the nodes value. Otherwise, you emit the bit <tt>PARENT</tt>, then
	 * emit the left and right node.
	 * </p>
	 *
	 * @param writer
	 *            A bit writer to write to
	 * @throws NullPointerException
	 *             if w is null
	 * @throws IOException
	 *             If there is a problem writing to the underlying stream
	 */
	public void writeHeader (BitWriter writer) throws IOException
	{
		
		writeHeaderHelper(writer,tree);
	}
	
	private void writeHeaderHelper(BitWriter writer, HuffmanNode n) throws IOException
	{
		if(n.isLeaf()){
			writer.writeBit(LEAF);
			writer.writeByte(n.getValue());
		}else{
			writer.writeBit(PARENT);
			writeHeaderHelper(writer,n.getLeft());
			writeHeaderHelper(writer, n.getRight());
		}
	}

	/**
	 * This method reads bits from the reader until the next codeword (from the
	 * given Reader) has been read in. It returns the byte that
	 * the code corresponds to. The data format for this is defined by
	 * <tt>encode</tt>
	 * 
	 * @param r
	 *            BitReader to read in the next codeword from
	 * @throws IOException
	 *             If there is an I/O error in the underlying reader. Also, if
	 *             the file contains invalid data or ends unexpectedly
	 * @throws NullPointerException
	 *             if r is null
	 * @return The data object read in
	 */
	public Byte decode (BitReader r) throws IOException
	{
		if(r==null)throw new NullPointerException();
		String bits = "";
		String bit = "";
		HuffmanNode ptr = tree;
		int i=0;
		while(!symbolList.containsValue(bits)){
			bit=Integer.toString(r.readBit());
			bits=bits.concat(bit);
		}
		ptr = tree; i=0;
		while(!ptr.isLeaf()){
			if(Integer.parseInt(Character.toString(bits.charAt(i)))==LEFT){
				ptr=ptr.getLeft();
			}
			else{
				ptr = ptr.getRight();
			}
			i++;
		}
		return ptr.getValue();
	}

	/**
	 * This method takes a data item emits the corresponding codeword.
	 * The bits <tt>LEFT</tt> and <tt>RIGHT</tt> are written so that
	 * if one takes that path in the Huffman tree they will get to the
	 * leaf node representing <tt>Item</tt>.
	 * 
	 * @param item
	 *            value to encode
	 * @param writer
	 *            BitWriter to write the code word (Huffman Code for that
	 *            string)
	 * @throws NullPointerException
	 *             if the item or writer is null.
	 * @throws IllegalArgumentException
	 *             if the item doesn't exist in this huffman coding
	 */
	public void encode (Byte item, BitWriter writer) throws IOException
	{
		
		if(item == null) throw new NullPointerException();
		if(!symbolList.containsKey(item)) throw new IllegalArgumentException();
		//reference arrayList with the bit sequence
		String s = symbolList.get(item);
		int length = s.length();
		//write out bits in the arrayList
		for(int i=0; i<length;i++){
			if(Integer.parseInt(Character.toString(s.charAt(i)))==LEFT) 
				writer.writeBit(LEFT);
			else
				writer.writeBit(RIGHT);
		}
		
	}

	/**
	 * Gets the root of the Huffman tree.  This is helpful for testing.
	 */
	public HuffmanNode getCodeTreeRoot ()
	{
		return tree;
	}
	
	private HuffCompare HComparator(){
		return new HuffCompare();
	}
	//HuffManNode comparator to pass into the instantiation of MyPriorityQueue
	private class HuffCompare implements Comparator<HuffmanNode>{

		public int compare(HuffmanNode hN1, HuffmanNode hN2) {
			if(hN1.getFreq() > hN2.getFreq()) return 1;
			if(hN1.getFreq() < hN2.getFreq()) return -1;
			return 0;
		}
		
	}
}
