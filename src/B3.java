import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class B3 {
	
	String mgfSeed;
	BigInteger maskLen;
	
	public B3(String mgfSeed, BigInteger maskLen){
		this.maskLen = maskLen;
		this.mgfSeed = mgfSeed;
	}
	
	public byte[] combineByteArrays(byte[] b1, byte[] b2){
		byte[] bytesCombined = new byte[b1.length+b2.length];
		System.arraycopy(b1, 0, bytesCombined, 0, b1.length);
		System.arraycopy(b2, 0, bytesCombined, b1.length, b2.length);
		System.out.println(Arrays.toString(bytesCombined));
		return bytesCombined;
	}
	
	public byte[] MGF1(String mgfSeed, BigInteger maskLen, BigInteger hLen) throws Exception{
		byte[] T = new byte[0];
		
		int digestLength = 0;
		int ceil = (maskLen.intValue() + hLen.intValue() - 1) / hLen.intValue();
		for(int counter = 0; counter < ceil; counter++){
			byte[] C = I2OSP(counter, new BigInteger("4"));
			byte[] hashInput = combineByteArrays(hexToByte(mgfSeed), C);
			T = combineByteArrays( T, Hash( hashInput ) );
		}
		byte[] answerCut = Arrays.copyOfRange(T, 0, maskLen.intValue());
		return answerCut;
	}
	
	public String byteToHex(byte[] b){
		return DatatypeConverter.printHexBinary(b);
	}
	
	public byte[] hexToByte(String s){
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public byte[] Hash(byte[] arr){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest(arr);
	}
	
	//SOURCE: http://stackoverflow.com/questions/4895523/java-string-to-sha1
	public static String byteArrayToHexString(byte[] b) {
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	  }
	  return result;
	}
	
	public byte[] I2OSP(int counter, BigInteger xLen){
		byte[] x = (new BigInteger(Integer.toString(counter))).toByteArray();
		byte[] octetString = new byte[xLen.intValue()];
		System.arraycopy(x, 0, octetString, octetString.length - x.length, x.length);
		return octetString;
	}
	
	public String run(){
		byte[] answer = new byte[0];
		try {
			answer = MGF1(mgfSeed, maskLen, new BigInteger("20"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//byte[] answerCut = Arrays.copyOfRange(answer, 0, maskLen.intValue());
		String returnAnswer = byteToHex(answer);
		System.out.println("ANSWER (maskLen bytes string): "+ returnAnswer);
		return returnAnswer;
	}

	//https://tools.ietf.org/html/rfc8017#appendix-B.2.1
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("mgfSeed: ");
		String mgfSeed = scan.nextLine();
		
		System.out.println("maskLen: ");
		BigInteger maskLen = BigInteger.valueOf(Long.decode(scan.nextLine()));//BigInteger.valueOf( Long.decode( "0x" + scan.nextLine() ) );
		
		B3 b3 = new B3(mgfSeed, maskLen);
		b3.run();

	}

}