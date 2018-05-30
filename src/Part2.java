import java.math.BigInteger;
import java.util.Random;

public class Part2 {

	public static String encrypt_message_sym(String msg, String password) {
		System.out.println("\n\nEncrypting... \n");
		final BigInteger random_z = new BigInteger(512, new Random());
		byte[] K = new byte[random_z.toByteArray().length + password.getBytes().length];
		System.arraycopy(random_z.toByteArray(), 0, K, 0, random_z.toByteArray().length);
		System.arraycopy(password.getBytes(), 0, K, random_z.toByteArray().length, password.getBytes().length);
		
		byte[] S = Util.asciiStringToByteArray("S");
        byte[] M = Util.asciiStringToByteArray("");
        int L = 1024;
		String ke_ka = new String(SHAKE.KMACXOF256(K, M, L, S));
		int mid = ke_ka.length()/2;
		String[] keys = {ke_ka.substring(0, mid), ke_ka.substring(mid)};
		String ke = keys[0];
		S = Util.asciiStringToByteArray("SKE");
		String c = new String(SHAKE.KMACXOF256(ke.getBytes(), M, msg.getBytes().length * 8, S));

		String c_out = Hex.xor_hex(Hex.bytes_to_hex(c.getBytes()), Hex.bytes_to_hex(msg.getBytes()));
		System.out.println("Generated z: " + random_z);
		System.out.println("Encrypted Message: " + c_out);
		return c_out;
	}
	
	public static String decrypt_message_sym(BigInteger random_z, String c, String password) {
		System.out.println("\n\n Decrypting... \n");
		byte[] K = new byte[random_z.toByteArray().length + password.getBytes().length];
		System.arraycopy(random_z.toByteArray(), 0, K, 0, random_z.toByteArray().length);
		System.arraycopy(password.getBytes(), 0, K, random_z.toByteArray().length, password.getBytes().length);
        byte[] S = Util.asciiStringToByteArray("S");
        byte[] M = Util.asciiStringToByteArray("");
        int L = 1024;
        String ke_ka = new String(SHAKE.KMACXOF256(K, M, L, S));
		int mid = ke_ka.length()/2;
		String[] keys = {ke_ka.substring(0, mid), ke_ka.substring(mid)};
		String ke = keys[0];
		S = Util.asciiStringToByteArray("SKE");
		String m = new String(SHAKE.KMACXOF256(ke.getBytes(), M, c.length()/2 * 8, S));
		
		System.out.println("c2: " + Hex.string_to_hex(m));
		System.out.println("c: " + c);
		
		String message = Hex.hex_to_string(Hex.xor_hex(Hex.string_to_hex(m), c));
		System.out.println(message);
		return message;
	}
	
}
