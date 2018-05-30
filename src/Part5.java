import java.math.BigInteger;

public class Part5 {
	
	public static String sig(String m, String pw) {
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
		BigInteger h = new BigInteger(h_byte).mod(EdwardCurve.p);
		
		BigInteger z = (k.subtract( (h.multiply(s)) )).mod(EdwardCurve.r);
		
		System.out.println("h: " + Hex.bytes_to_hex(h.toByteArray()));
		System.out.println("z: " + z);
		
		return Hex.bytes_to_hex(h.toByteArray());
	}
	
	public static String verify(BigInteger h, BigInteger z, EdwardPoint V, String m) {
		EdwardPoint G = new EdwardPoint(new BigInteger("18"));
		EdwardPoint U = G.exponentiation(z, G);
		U.add(V.exponentiation(h, V));
		
		byte[] K = U.x.toByteArray();
		byte[] X = Util.asciiStringToByteArray(m);
		int L = 512;
		byte[] S = Util.asciiStringToByteArray("T");
		
		byte[] h_byte = SHAKE.KMACXOF256(K, X, L, S);
		BigInteger h_p = new BigInteger(h_byte).mod(EdwardCurve.p);
		
		System.out.println("h_prime = " + h_p);
		
		return Hex.bytes_to_hex(h_p.toByteArray());
		
	}
	
}
