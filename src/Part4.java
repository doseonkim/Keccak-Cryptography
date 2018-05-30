import java.math.BigInteger;
import java.security.spec.ECPoint;
import java.util.Random;

public class Part4 {

	public static String encrypt_public_key(String m, EdwardPoint V) {		
		BigInteger random_k = new BigInteger(512, new Random());
		random_k = random_k.multiply(new BigInteger("4")).mod(EdwardCurve.p);
		EdwardPoint W = V.exponentiation(random_k, V);
		
		EdwardPoint G = new EdwardPoint(new BigInteger("18"));
		EdwardPoint Z = G.exponentiation(random_k, G);

		byte[] K = W.x.toByteArray();
		byte[] M = Util.asciiStringToByteArray("");
		byte[] S = Util.asciiStringToByteArray("P");
        int L = 1024;
		String ke_ka = new String(SHAKE.KMACXOF256(K, M, L, S));
		int mid = ke_ka.length()/2;
		String[] keys = {ke_ka.substring(0, mid), ke_ka.substring(mid)};
		String ke = keys[0];
		S = Util.asciiStringToByteArray("PKE");
		
		byte[] prng = (SHAKE.KMACXOF256(ke.getBytes(), M, m.length() * 8, S));

		String c_out = Hex.xor_hex(Hex.bytes_to_hex(prng), Hex.string_to_hex(m));
		
		System.out.println("encrypted: " + c_out);	
		
		decrypt_public_key(c_out, Z);
		
		return c_out;
	}
	
	public static void decrypt_public_key(String c, EdwardPoint Z) {
		byte[] pw = Util.asciiStringToByteArray("test");
		byte[] X = Util.asciiStringToByteArray("");
		int L = 512;
		byte[] S = Util.asciiStringToByteArray("K");
		
		byte[] s_byte = SHAKE.KMACXOF256(pw, X, L, S);
		
		BigInteger priv_key = new BigInteger(s_byte);
		BigInteger s = priv_key.multiply(new BigInteger("4")).mod(EdwardCurve.p);
		
		EdwardPoint W = Z.exponentiation(s, Z);

		byte[] K = W.x.toByteArray();
		byte[] M = Util.asciiStringToByteArray("");
		S = Util.asciiStringToByteArray("P");
        L = 1024;
		String ke_ka = new String(SHAKE.KMACXOF256(K, M, L, S));
		int mid = ke_ka.length()/2;
		String[] keys = {ke_ka.substring(0, mid), ke_ka.substring(mid)};
		String ke = keys[0];
		S = Util.asciiStringToByteArray("PKE");
		
		byte[] prng = SHAKE.KMACXOF256(ke.getBytes(), M, c.length()/2 * 8, S);
		
		String message = Hex.hex_to_string(Hex.xor_hex(Hex.bytes_to_hex(prng), c));
		
		System.out.println("decrypted: " + message);	
	}
}
