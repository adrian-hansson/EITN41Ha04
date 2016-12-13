import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class B4 {

	int n,e, mByteLength, hLen, mLen;
	B3 b3;
	
	public B4(){
		b3 = new B3(null, null);
		mByteLength = 128;
		hLen = 20;
	}
	
	public String byteToHex(byte[] b){
		return DatatypeConverter.printHexBinary(b);
	}
	
	public byte[] hexToByte(String s){
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public byte[] SHA1(byte[] arr){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest(arr);
	}
	
	public byte[] combineByteArrays(byte[] b1, byte[] b2){
		byte[] bytesCombined = new byte[b1.length+b2.length];
		System.arraycopy(b1, 0, bytesCombined, 0, b1.length);
		System.arraycopy(b2, 0, bytesCombined, b1.length, b2.length);
		System.out.println(Arrays.toString(bytesCombined));
		return bytesCombined;
	}
	
	public String OAEP_encode(String M, String seed){
		System.out.println("M to encode: "+M);
		System.out.println("Seed used: "+seed);
		byte[] bytesM = hexToByte(M);
		mLen = bytesM.length;
		
		String L = "";
		
		byte[] lHash = SHA1(L.getBytes());
		
		int psLength = mByteLength - mLen - 2*hLen - 2;
		byte[] psPadding = new byte[psLength];
		
		int maskLen = mByteLength - hLen - 1;
		
		byte[] DB = combineByteArrays(lHash, psPadding);
		DB = combineByteArrays(DB, new byte[] {0x01});
		DB = combineByteArrays(DB, bytesM);
		
		System.out.println("DB Length = "+DB.length);
		int theoDBLen = mByteLength - hLen - 1;
		System.out.println("Theoretical DB Length = " + theoDBLen);
		
		byte[] seedBytes = hexToByte(seed);
		
		//e
		byte[] dbMask = new byte[0];
		try {
			dbMask = b3.MGF1(seed, new BigInteger(Integer.toString(maskLen)), new BigInteger(Integer.toString(hLen)));
		} catch (Exception e) {
			System.out.println("dbMask: " + Arrays.toString(dbMask));
			e.printStackTrace();
		}
		
		//f
		byte[] maskedDB = xor(DB, dbMask);
		
		//g
		byte[] seedMask = new byte[0];
		try {
			seedMask = b3.MGF1(byteToHex(maskedDB), new BigInteger(Integer.toString(hLen)), new BigInteger(Integer.toString(hLen)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//h
		byte[] maskedSeed = xor(seedBytes, seedMask);
		
		//i
		byte[] EM = combineByteArrays(new byte[] {0x00}, maskedSeed);
		EM = combineByteArrays(EM, maskedDB);
		System.out.println("ENCODE ANSWER: " + byteToHex(EM));
		return byteToHex(EM);
	}
	
	public byte[] xor(byte[] a, byte[] b){
		System.out.println("a lengt: "+a.length);
		System.out.println("b lengt: "+b.length);
		System.out.println("MGF lengt: "+hLen);
		byte[] xored = new byte[a.length];
		for(int i = 0; i < xored.length; i++){
			xored[i] = (byte) (a[i] ^ b[i]);
		}
		return xored;
	}
	
	public String OAEP_decode(String EM){
		//a
		int hLen = 20;
		String L = "";
		byte[] bytesEM = hexToByte(EM);
		byte[] lHash = SHA1(hexToByte(L));
		//b
		byte[] maskedSeed = new byte[hLen];
		System.arraycopy(bytesEM, 1, maskedSeed, 0, maskedSeed.length);
		byte[] maskedDB = new byte[mByteLength-hLen-1];
		System.arraycopy(bytesEM, maskedSeed.length+1, maskedDB, 0, maskedDB.length);
		//c
		byte[] seedMask = new byte[0];
		try {
			seedMask = b3.MGF1(byteToHex(maskedDB), BigInteger.valueOf(hLen), BigInteger.valueOf(hLen));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//d
		byte[] seed = xor(maskedSeed, seedMask);
		//e
		byte[] dbMask = new byte[0];
		try {
			dbMask = b3.MGF1(byteToHex(seed), BigInteger.valueOf(128-hLen-1), BigInteger.valueOf(hLen));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//f
		byte[] DB = xor(maskedDB, dbMask);
		//g
		Byte b = new Byte((byte) 0x01);
		byte[] M = new byte[0];
		for (int i = lHash.length; i < DB.length; i++) {
			Byte currByte = DB[i];
			if (currByte.compareTo(b) == 0) {
				M = new byte[DB.length - i -1];
				System.arraycopy(DB, 1+i, M, 0, M.length);
			}
		}
		System.out.println("DECODE ANSWER: "+byteToHex(M));
		return byteToHex(M);
	}
	
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.println("M: ");
		String M = scan.nextLine();
		System.out.println("seed: ");
		String seed = scan.nextLine();
		System.out.println("EM: ");
		String EM = scan.nextLine(); //use for decode
		
		B4 b4 = new B4();
		String encodeRes = b4.OAEP_encode(M, seed);
		String decodeRes = b4.OAEP_decode(EM);
		System.out.println("Encode res: "+encodeRes);
		System.out.println("Decode res: "+decodeRes);
	}
}
