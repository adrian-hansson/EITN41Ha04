import java.math.BigInteger;

public class B2OTR {
	
	public B2OTR(){
		
	}
	
	public String toHexStr(BigInteger i){
		return i.toString(16);
	}
	
	public void run(){
		
	}
	
	public static void main(String[] args){
		B2OTR otr = new B2OTR();
		otr.run();
		
		Client client = new Client();
		client.run();
	}

}
