import java.math.BigInteger;

public class Part5 {
	
	public static void sig(String m, String pw) {
		byte[] K = Util.asciiStringToByteArray(pw);
		byte[] X = Util.asciiStringToByteArray("");
		int L = 512;
		byte[] S = Util.asciiStringToByteArray("K");
		
		byte[] s_byte = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger s = new BigInteger(s_byte);
		s = s.multiply(new BigInteger("4")).mod(EdwardCurve.p);

		K = s.toByteArray();
		X = Util.asciiStringToByteArray(m);
		S = Util.asciiStringToByteArray("N");
		
		byte[] k_byte = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger k = new BigInteger(k_byte);
		k = k.multiply(new BigInteger("4")).mod(EdwardCurve.p);
		
		EdwardPoint G = new EdwardPoint(new BigInteger("18"));
		EdwardPoint U = G.exponentiation(k, G);
		
		K = U.x.toByteArray();
		S = Util.asciiStringToByteArray("T");
		
		byte[] h_byte = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger h = new BigInteger(h_byte);
		
		BigInteger z = (k.subtract( (h.multiply(s)) )).mod(EdwardCurve.r);
		
		
		
		System.out.println("h: " + h.mod(EdwardCurve.p));
		System.out.println("z: " + z);
		
		 BigInteger pk_x = new BigInteger("1786809195834544992531552625415921588753052165893517486576193572575579846804266712741676196639621651803122494070269213479238747552723481188673412143480793455");
	     BigInteger pk_y = new BigInteger("5491406845570931661325960199719625282179988627608682193642176813472048617409894797587157292417823701092031880143115157168807218699475574568673812334906447462");
	     EdwardPoint V = new EdwardPoint(pk_x, pk_y);
	     
	     verify(h, z, V, m);
		
	}
	
	public static void verify(BigInteger h, BigInteger z, EdwardPoint V, String m) {
		EdwardPoint G = new EdwardPoint(new BigInteger("18"));
		EdwardPoint U = G.exponentiation(z, G);
		U.add(V.exponentiation(h, V));
		
		byte[] K = U.x.toByteArray();
		byte[] X = Util.asciiStringToByteArray(m);
		int L = 512;
		byte[] S = Util.asciiStringToByteArray("T");
		
		byte[] h_byte = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger h_p = new BigInteger(h_byte);
		
		System.out.println("h_prime = " + h_p);
		
	}
	
}
