
public class Part1 {

	public static String KMAC_hash(String m) {
    	final int L = 512;
        final byte[] K = Util.asciiStringToByteArray("");
        final byte[] S = Util.asciiStringToByteArray("D");

        final byte[] M = Util.asciiStringToByteArray(m);
        
        byte[] hash = SHAKE.KMACXOF256(K, M, L, S);
        
        return Hex.bytes_to_hex(hash);
    }
}
