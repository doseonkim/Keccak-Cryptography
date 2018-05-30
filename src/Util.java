import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Util {

	/**
     * Convert an ASCII string to a byte array.
     * @param s string to convert to a byte array
     * @return  s converted to a byte array (all characters must be in range [0..255])
     */
    static byte[] asciiStringToByteArray(String s) {
        byte[] val = new byte[s.length()];
        for (int i = 0; i < val.length; i++) {
            char c = s.charAt(i);
            if (c >= 256) {
                throw new RuntimeException("Non-ASCII character found");
            }
            val[i] = (byte)c;
        }
        return val;
    }
    
    public static String read_file(String file_location) {
		String data = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file_location)))
        {
            String sCurrentLine;
            if ((sCurrentLine = br.readLine()) != null) {
            	data = sCurrentLine;
            }
            while ((sCurrentLine = br.readLine()) != null) {
                data += "\n" + sCurrentLine;
            }
        } catch (IOException e) {
        	System.out.println("Sorry the file could not be found.");
        }
		return data;
	}
}
