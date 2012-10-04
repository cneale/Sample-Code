package edu.cmu.cs211.compression.bw;

import edu.cmu.cs211.compression.Transformer;


public class MoveToFrontTransformer extends Transformer {
	
	/*
	 * Invert the Move-To-Front transformation
	 * @param input
	 * 			byte sequence from the output for transform()
	 * @return
	 * 			byte sequence after inverting the transformation
	 */
	@Override
	public byte[] invertTransform(byte[] input) 
	{
		byte index;
		int[] dictionary = new int[256];
		int cnt=0;
		byte[] output = new byte[input.length];
		
		for(int i=0; i<dictionary.length; i++) dictionary[i] = i;
		for(int j=0; j<input.length;j++){
			index=input[cnt++];
			output[j] = (byte)(dictionary[index & 0xFF]);
			for(int k= (index & 0xFF); k>0; k--){
				dictionary[k]= dictionary[k-1];
			}
			dictionary[0]= index & 0xFF;
		}
		return output;
	}

	/*
	 * Transform the byte sequence 
	 * according to the MoveToFront transformation
	 * @param input byte sequence
	 * @return transformed byte sequence
	 * @throws NullPointerException
	 */
	@Override
	public byte[] transform(byte[] input) {
		int index;
		byte[] dictionary = new byte[256];
		byte temp;
		byte[] output = new byte[input.length];
		for(int i=0; i<dictionary.length;i++){
			dictionary[i] = (byte)(i & 0xFF);
		}
		for(int i=0;i<input.length;i++){
			index = 0;
			while((dictionary[index])!=(input[i]&0xFF)){
				index++;
			}
			temp = dictionary[index];
			output[i] = (byte)(index & 0xFF);
			for(int j=index; j>0;j--){
				dictionary[j] = dictionary[j-1];
			}
			dictionary[0] = temp;
		}
		return output;
	}

}
