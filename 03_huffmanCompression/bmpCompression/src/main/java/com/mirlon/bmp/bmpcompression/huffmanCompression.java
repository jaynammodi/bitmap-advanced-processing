/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mirlon.bmp.bmpcompression;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;

/**
 *
 * @author 
 */
public class huffmanCompression {
    static PriorityQueue<TREE> pq = new PriorityQueue<>();
    static int[] freq = new int[300];
    static String[] ss = new String[300];
    static int exbits;
    static byte bt;
    static int cnt; 
    
    // Binary Tree Implementation
    static class TREE implements Comparable<TREE> {
		TREE Lchild;
		TREE Rchild;
		public String deb;
		public int Bite;
		public int Freqnc;

		public int compareTo(TREE T) {
			if (this.Freqnc < T.Freqnc)
				return -1;
			if (this.Freqnc > T.Freqnc)
				return 1;
			return 0;
		}
	}

	static TREE Root;
        
        // Huffman Encoding Frequency Calculation, reading the binary file and calculating the character frequency table
        public static void CalFreq(String fname) {
		File file = null;
		Byte bt;

		file = new File(fname);
		try {
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			while (true) {
				try {

					bt = data_in.readByte();
					freq[to(bt)]++;
				} catch (EOFException eof) {
					System.out.println(" > Encountered End of File");
					break;
				}
			}
			file_input.close();
			data_in.close();
		} catch (IOException e) {
			System.out.println(" > Huffman Compression IO Exception =: " + e);
		}
		file = null;
	}
        
        // Utility Functions
        
        public static int to(Byte b) {
		int ret = b;
		if (ret < 0) {
			ret = ~b;
			ret = ret + 1;
			ret = ret ^ 255;
			ret += 1;
		}
		return ret;
	}
        
        public static void initHzipping() {
		int i;
		cnt = 0;
		if (Root != null)
			fredfs(Root);
		for (i = 0; i < 300; i++)
			freq[i] = 0;
		for (i = 0; i < 300; i++)
			ss[i] = "";
		pq.clear();
	}

	// DFS through the Huffman Tree to clean up
	public static void fredfs(TREE now) {

		if (now.Lchild == null && now.Rchild == null) {
			now = null;
			return;
		}
		if (now.Lchild != null)
			fredfs(now.Lchild);
		if (now.Rchild != null)
			fredfs(now.Rchild);
	}

	// DFS to generate Huffman Encoding
	public static void dfs(TREE now, String st) {
		now.deb = st;
		if ((now.Lchild == null) && (now.Rchild == null)) {
			ss[now.Bite] = st;
			return;
		}
		if (now.Lchild != null)
			dfs(now.Lchild, st + "0");
		if (now.Rchild != null)
			dfs(now.Rchild, st + "1");
	}

	// Generating the nodes of the priority queue using all the leaves of the tree
	public static void MakeNode() {
		int i;
		pq.clear();

		for (i = 0; i < 300; i++) {
			if (freq[i] != 0) {
				TREE Temp = new TREE();
				Temp.Bite = i;
				Temp.Freqnc = freq[i];
				Temp.Lchild = null;
				Temp.Rchild = null;
				pq.add(Temp);
				cnt++;
			}

		}
		TREE Temp1, Temp2;

		if (cnt == 0) {
			return;
		} else if (cnt == 1) {
			for (i = 0; i < 300; i++)
				if (freq[i] != 0) {
					ss[i] = "0";
					break;
				}
			return;
		}

		// will there b a problem if the file is empty
		// a bug is found if there is only one character
		while (pq.size() != 1) {
			TREE Temp = new TREE();
			Temp1 = pq.poll();
			Temp2 = pq.poll();
			Temp.Lchild = Temp1;
			Temp.Rchild = Temp2;
			Temp.Freqnc = Temp1.Freqnc + Temp2.Freqnc;
			pq.add(Temp);
		}
		Root = pq.poll();
	}

        // generating a fake storage file to write the Huffman Encoded Contents
	public static long buildHBmp(String fname) {

		File filei, fileo;
		int i;
                Byte btt;


		filei = new File(fname);
		fileo = new File(fname + ".huffman.bmp");
		try {
			FileInputStream file_input = new FileInputStream(filei);
			DataInputStream data_in = new DataInputStream(file_input);
			PrintStream ps = new PrintStream(fileo);
                        FileOutputStream file_output = new FileOutputStream(fileo);
			DataOutputStream data_out = new DataOutputStream(file_output);

			data_out.writeInt(cnt);
			for (i = 0; i < 256; i++) {
				if (freq[i] != 0) {
					btt = (byte) i;
					data_out.write(btt);
					data_out.writeInt(freq[i]);
				}
			}
			long texbits;
			texbits = filei.length() % 8;
			texbits = (8 - texbits) % 8;
			exbits = (int) texbits;
			data_out.writeInt(exbits);
			while (true) {
				try {
					bt = 0;
					byte ch;
					for (exbits = 0; exbits < 8; exbits++) {
						ch = data_in.readByte();
						bt *= 2;
						if (ch == '1')
							bt++;
					}
					data_out.write(bt);

				} catch (EOFException eof) {
					int x;
					if (exbits != 0) {
						for (x = exbits; x < 8; x++) {
							bt *= 2;
						}
						data_out.write(bt);
					}

					exbits = (int) texbits;
					System.out.println(" > Huffman | Extrabits: " + exbits);
					System.out.println(" > Encountered End of File");
					break;
				}
			}
			data_in.close();
			data_out.close();
			file_input.close();
			file_output.close();
			System.out.println(" > Huffman(1) | Output File Size: " + fileo.length());

		} catch (IOException e) {
			System.out.println(" > Realzip IO exception = " + e);
		}
                return fileo.length();

	}

//	// Parsing the Huffman Codes from the fakezipped to generate huffman encoded BMP and return Size after Compression
//	public static long realzip(String fname, String fname1) {
//		File filei, fileo;
//		int i, j = 10;
//		Byte btt;
//
//		filei = new File(fname);
//		fileo = new File(fname1);
//
//		try {
//			FileInputStream file_input = new FileInputStream(filei);
//			DataInputStream data_in = new DataInputStream(file_input);
//			FileOutputStream file_output = new FileOutputStream(fileo);
//			DataOutputStream data_out = new DataOutputStream(file_output);
//
//			data_out.writeInt(cnt);
//			for (i = 0; i < 256; i++) {
//				if (freq[i] != 0) {
//					btt = (byte) i;
//					data_out.write(btt);
//					data_out.writeInt(freq[i]);
//				}
//			}
//			long texbits;
//			texbits = filei.length() % 8;
//			texbits = (8 - texbits) % 8;
//			exbits = (int) texbits;
//			data_out.writeInt(exbits);
//			while (true) {
//				try {
//					bt = 0;
//					byte ch;
//					for (exbits = 0; exbits < 8; exbits++) {
//						ch = data_in.readByte();
//						bt *= 2;
//						if (ch == '1')
//							bt++;
//					}
//					data_out.write(bt);
//
//				} catch (EOFException eof) {
//					int x;
//					if (exbits != 0) {
//						for (x = exbits; x < 8; x++) {
//							bt *= 2;
//						}
//						data_out.write(bt);
//					}
//
//					exbits = (int) texbits;
//					System.out.println(" > Huffman | Extrabits: " + exbits);
//					System.out.println(" > Encountered End of File");
//					break;
//				}
//			}
//			data_in.close();
//			data_out.close();
//			file_input.close();
//			file_output.close();
//			System.out.println(" > Huffman(1) | Output File Size: " + fileo.length());
//
//		} catch (IOException e) {
//			System.out.println(" > Realzip IO exception = " + e);
//		}
//		filei.delete();
//                return fileo.length();
//        }

	/*******************************************************************************/

	/*
	 * public static void main (String[] args) { initHzipping();
	 * CalFreq("in.txt"); // calculate the frequency of each digit MakeNode();
	 * // makeing corresponding nodes if(cnt>1) dfs(Root,""); // dfs to make the
	 * codes fakezip("in.txt"); // fake zip file which will have the binary of
	 * the input to fakezipped.txt file
	 * realzip("fakezipped.txt","in.txt"+".huffz"); // making the real zip
	 * according the fakezip.txt file initHzipping();
	 * 
	 * }
	 */

	public static long beginHCompression(String arg1) {
		initHzipping();
		CalFreq(arg1); // calculate the frequency of each digit
		MakeNode(); // makeing corresponding nodes
		if (cnt > 1)
			dfs(Root, ""); // dfs to make the codes
		return buildHBmp(arg1); // 
        }
}
