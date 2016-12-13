import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class B4 {

	int n,e, answerLength, hLen, mLen;
	B3 b3;
	
	public B4(){
		b3 = new B3(null, null);
		answerLength = 128; //since we have 128byte encoded message length as per specs
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
		
		int psLength = answerLength - mLen - 2*hLen - 2;
		byte[] psPadding = new byte[psLength];
		
		int maskLen = answerLength - hLen - 1;
		
		byte[] DB = combineByteArrays(lHash, psPadding);
		DB = combineByteArrays(DB, new byte[1]);
		DB = combineByteArrays(DB, bytesM);
		//DB = combineByteArrays(DB, new byte[maskLen-DB.length]);//TODO: No?
		
		System.out.println("DB Length = "+DB.length);
		int theoDBLen = answerLength - hLen - 1;
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
		byte[] maskedDB = new byte[DB.length];//DB ^ dbMask;
		System.out.println("DB: "+DB.length);
		System.out.println("dbMask: "+dbMask.length);
		int forLi = answerLength-hLen-1;
		System.out.println("For limit: "+forLi);
		for(int i = 0; i < answerLength-hLen-1; i++){
			maskedDB[i] = (byte) (DB[i] ^ dbMask[i]);
		}
		
		//g
		byte[] seedMask = new byte[0];
		try {
			seedMask = b3.MGF1(byteToHex(maskedDB), new BigInteger(Integer.toString(hLen)), new BigInteger(Integer.toString(hLen)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//h
		byte[] maskedSeed = new byte[seedBytes.length];
		for(int i = 0; i < hLen; i++){
			maskedSeed[i] = (byte) (seedBytes[i] ^ seedMask[i]);
		}
		
		//i
		System.out.println("maskedSeed into EM: "+Arrays.toString(maskedSeed));
		System.out.println("maskedDB into EM: "+Arrays.toString(maskedDB));
		byte[] EM = combineByteArrays(new byte[1], maskedSeed);
		EM = combineByteArrays(EM, maskedDB);
		
		//String EM = ""; //Put the final answer here
		System.out.println("B4 ANSWER:" + byteToHex(EM));
		return byteToHex(EM);
	}
	
	public String OAEP_decode(String EM){
		return "Decode not working";
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
		//b4.OAEP_encode("c107782954829b34dc531c14b40e9ea482578f988b719497aa0687", "1e652ec152d0bfcd65190ffc604c0933d0423381");
		b4.OAEP_encode(M, seed);
	}
}
