package edu.cmu.cs211.compression.bw;

import java.util.Arrays;
import java.util.Collections;

import edu.cmu.cs211.compression.Transformer;

public class BurrowsWheelerTransformer extends Transformer {
	
	
	/*
	 * Invert the transformed sequence into the original text
	 */
	@Override
	public byte[] invertTransform(byte[] input)
	{
		int start = (int)input[0];
		int cnt=0;
		int num;
		byte[] sansInt = new byte[input.length-4];
		byte[] sorted = new byte[input.length-4];
		byte[] output = new byte[input.length-4];
		
		
		for(int i=0; i<sansInt.length;i++){
			sansInt[i]=input[i+4];
			sorted[i]=input[i+4];
		}
		
		Arrays.sort(sorted);
		while(cnt<output.length){
			output[cnt++] = sorted[start];
			num=1;
			for(int i=0; i<start;i++){
				if(sorted[i]==sorted[start]){
					num++;
				}
			}
			//find in sansInt
			int p=0;
			while(num>0){
				if(sansInt[p++]==sorted[start])
					num--;
			}
			start = p-1;
		}
		return output;
	}

	/*
	 * Transform the byte sequence from the reader using Burrows-Wheeler transformation
	 * Output the header (startIndex) and the transformed sequence to the writer
	 */
	@Override
	public byte[] transform (byte[] input)
	{
		int length = input.length;
		int lastCol, rowInMatrix=0;
		int[] reference = new int[length];
		byte[] temp1 = new byte[length];
		byte[] temp2 = new byte[length];
		byte[] output = new byte[length+4];
		int temp, highestIndex=0;
		java.nio.ByteBuffer buf1,buf2;
		int cnt;
		
		for(int j=0;j<length;j++) reference[j]=j;
		
		for(int i=0;i<length-1;i++){
			cnt=i;
			while(cnt<length-1){
				rotateArray(input, temp1,cnt++);
				rotateArray(input,temp2,cnt);
				buf1 = java.nio.ByteBuffer.wrap(temp1);
				buf2 = java.nio.ByteBuffer.wrap(temp2);
				if(buf2.compareTo(buf1) > 0) highestIndex = cnt;
			}
			temp = reference[i];
			reference[i]=highestIndex;
			reference[highestIndex] = temp;
		}
		
		for(int i=0; i<length;i++){
			if(reference[i]==0){
				rowInMatrix=i;
			}
		}
		
		for(int k=0; k<length;k++){
			lastCol = (reference[k] == 0) ? length-1: reference[k]-1;
			output[k+4] = input[lastCol];
		}
		output[0]=(byte)rowInMatrix;
		return output;
	}
		
	private void rotateArray(byte[] original, byte[] output, int rotations){
		int j=0;
		for(int i=rotations; i < original.length; i++){
			output[i] = original[j++];
		}
		for(int p=0; p<rotations; p++){
			output[p]=original[j++];
		}
	}
	
}

