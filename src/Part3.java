import java.math.BigInteger;
import java.security.spec.ECPoint;

public class Part3 {
			
	public static EdwardPoint generate_key_pair(String pw) {
	    
		byte[] K = Util.asciiStringToByteArray(pw);
		byte[] X = Util.asciiStringToByteArray("");
		byte[] S = Util.asciiStringToByteArray("K");
		int L = 512;
		byte[] s = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger priv_key = new BigInteger(s);
		priv_key = priv_key.multiply(new BigInteger("4"));
		priv_key = priv_key.mod(EdwardCurve.p);
		
		System.out.println("[Part 3] S: " + priv_key);
		
		EdwardPoint ed = new EdwardPoint(new BigInteger("18"));
		
		EdwardPoint pub_key = ed.exponentiation(priv_key, ed);
		
		System.out.println("public key x: " + pub_key.x);
		
		System.out.println("public key y: " + pub_key.y);
		
		return pub_key;
	}

}
