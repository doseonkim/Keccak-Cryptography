import javax.xml.bind.DatatypeConverter;

public class Hex {

	/**
	 * Available HEX characters 0-F.
	 */
	private static final char[] HEX_CHARS =
		{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	
	public static String bytes_to_hex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    public static String xor_hex(String a, String b) {
	    char[] chars = new char[a.length()];
	    for (int i = 0; i < chars.length; i++) {
	        chars[i] = to_hex(from_hex(a.charAt(i)) ^ from_hex(b.charAt(i)));
	    }
	    return new String(chars);
	}
    
    private static int from_hex(char c) {
	    if (c >= '0' && c <= '9') {
	        return c - '0';
	    }
	    if (c >= 'A' && c <= 'F') {
	        return c - 'A' + 10;
	    }
	    if (c >= 'a' && c <= 'f') {
	        return c - 'a' + 10;
	    }
	    throw new IllegalArgumentException();
	}

	private static char to_hex(int n) {
	    if (n < 0 || n > 15) {
	        throw new IllegalArgumentException();
	    }
	    return "0123456789ABCDEF".charAt(n);
	}
    
	/**
	 * converts string hex, overload function.
	 * @param s String to convert to hex.
	 * @return Hex value in string format.
	 */
	public static String string_to_hex(String s) {
		return bytes_to_hex(s.getBytes());
	}
	
	/**
	 * Converts string to hex.
	 * @param data data of string in bytes.
	 * @return Hex value in string format.
	 */
	/*public static String string_to_hex(byte[] data) {
		int len = data.length;
		char[] output = new char[len << 1];
		for(int i = 0, j = 0; i < len; i++) {
			output[j++] = HEX_CHARS[(0xF0 & data[i]) >>> 4];
            output[j++] = HEX_CHARS[0x0F & data[i]];
		}
		return new String(output);
	}*/
    
	public static String hex_to_string(String hex){
		  StringBuilder sb = new StringBuilder();
		  StringBuilder temp = new StringBuilder();
		  
		  for( int i=0; i<hex.length()-1; i+=2 ){
		      String output = hex.substring(i, (i + 2));
		      int decimal = Integer.parseInt(output, 16);
		      sb.append((char)decimal);
			  
		      temp.append(decimal);
		  }
		  return sb.toString();
	  }
}
