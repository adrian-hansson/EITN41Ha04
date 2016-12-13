import java.net.*;
import java.io.*;
import java.math.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class Client {
//	public static void main(String[] args) {
//		new Client().run();
//	}
	
	public byte[] hash(byte[] s1, byte[] s2){
		byte[] bytesToHash;
		bytesToHash = new byte[s1.length+s2.length]; //correct?
		System.arraycopy(s1, 0, bytesToHash, 0, s1.length);
		System.arraycopy(s2, 0, bytesToHash, s1.length, s2.length);
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest(bytesToHash);
	}
	
	public BigInteger randBigInt(){
		Random rand = new Random();
		int theRandom = rand.nextInt();
		if(theRandom < 0){
			theRandom = theRandom * (-1);
		}
		if(theRandom == 0){
			return randBigInt();
		}
		return new BigInteger(Integer.toString( theRandom ));
	}

	void run() {
		String serverName = "eitn41.eit.lth.se";
		int port = 1337;
		Random rnd = new Random();
		
		String msg = "0123456789abcdef";
		
		// the p shall be the one given in the manual
		BigInteger p = new BigInteger("1234567890abcdef", 16);
		p = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA237327FFFFFFFFFFFFFFFF",16);
		BigInteger g = new BigInteger("2");

		try {
			Socket client = new Socket(serverName, port);
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			// receive g**x1 and convert to a number
			String g_x1_str = in.readLine();
			System.out.println("g**x1: " + g_x1_str);
			BigInteger g_x1 = new BigInteger(g_x1_str, 16);

			// generate g**x2, x2 shall be a random number
			//String randomX2 = Integer.toString(rnd.nextInt());
			BigInteger x2 = randBigInt();
			// calculate g**x2 mod p
			BigInteger g_x2 = g.modPow(x2, p);
			// convert to hex-string and send.
			out.println(g_x2.toString(16));
			// read the ack/nak. This should yield a nak due to x2 being 0
			System.out.println("\nsent g_x2: " + in.readLine());
			BigInteger g2a = new BigInteger(in.readLine(), 16);
			
			//DIFFIE HELLMAN KEY
			BigInteger diffieKey = g_x1.modPow(x2, p);
			
			//<-----
			BigInteger b2 = randBigInt();
			BigInteger g2b = g.modPow(b2, p);
			out.println(g2b.toString(16));
			//----->
			System.out.println("Confirm g2b: "+in.readLine());
			
			//----->
			BigInteger g3a = new BigInteger(in.readLine(), 16);
			
			//<-----
			BigInteger b3 = randBigInt();
			BigInteger g3b = g.modPow(b3,  p);
			out.println(g3b.toString(16));
			//----->
			System.out.println("Confirmation g3b: "+in.readLine());
			
			//----->
			BigInteger Pa = new BigInteger(in.readLine(), 16);
			
			//<-----
			String passphrase = "eitn41 <3";
			BigInteger y = new BigInteger( 1, hash(diffieKey.toByteArray(), passphrase.getBytes()));
			BigInteger r = randBigInt();
			System.out.println("b2: "+b2);
			System.out.println("p: "+p);
			BigInteger g2 = g2a.modPow(b2, p);
			BigInteger g3 = g3a.modPow(b3, p);
			BigInteger Pb = g3.modPow(r, p);
			out.println(Pb.toString(16));
			
			//----->
			System.out.println("ACK/NACK Pb: "+in.readLine());
			
			//----->
			BigInteger Qa = new BigInteger(in.readLine(), 16);
			
			//<-----
			BigInteger Qb = g.modPow(r, p).multiply(g2.modPow(y, p));
			out.println(Qb.toString(16));
			
			//----->
			System.out.println("ACK/NACK Qb: "+in.readLine());
			
			//----->
			BigInteger Ra = new BigInteger(in.readLine(), 16);
			System.out.println("Ra: "+Ra);
			
			//<-----
			BigInteger Rb = Qa.multiply(Qb.modInverse(p)).modPow(b3, p);//Qb.modInverse(p).multiply(Qa).modPow(b3, p);
			out.println(Rb.toString(16));
			
			//----->
			System.out.println("ACK/NACK Rb: "+in.readLine());
			
			//----->
			System.out.println("Authentication: "+in.readLine());

			//<-----
			BigInteger Rab = Ra.modPow(b3, p); //??
			BigInteger messageToSend = new BigInteger(msg,16);
			out.println(messageToSend.xor(diffieKey).toString(16));
			
//			if(Rab.compareTo(Pa.divide(Pb)) == 0){ //if they're equal
//				out.println(Rb);
//			}
//			else{
//				System.out.println("Rab NOT equal to Pa/Pb");
//				client.close();
//				throw new Exception("Rab NOT equal to Pa/Pb");
//			}
			
			//----->
			System.out.println("FINAL RESPONSE: "+in.readLine());

			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e2){
			e2.printStackTrace();
		}
	}
}
