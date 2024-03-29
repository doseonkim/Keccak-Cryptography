import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECPoint;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

/**
 * Test driver for cSHAKE256 and KMACXOF256.
 *
 * @author Markku-Juhani Saarinen (original Keccak and SHAKE implementation in C)
 * @author Paulo S. L. M. Barreto (Java version, cSHAKE, KMACXOF)
 */
public class Main {
	
	/**
	 * Available prompts for console options.
	 */
	public static final String WELCOME_MESSAGE = "Welcome to TCSS 487 Cryptography, please shoose one of the following options :";
	
	public static final String[] OPTIONS = {"0) Exit", "1) Hash File", "2) Hash Input Text", 
			"3) Encrypt File Symmetrically", "4) Decrypt File Symmetrically", "5) Create Elliptic Curve Key Pair",
			"6) Encrypt File With Public Key", "7) Decrypt File Encrypted With Public Key", 
			"8) Sign a file with a given password", "9) Verify a file and a signature file with Private Key" 
	};
	
	public static final String FILE_INPUT_MESSAGE = "Please input the location of the file you would like to alter: ";
	
	public static final String TEXT_INPUT_MESSAGE = "Please input the text you would like to alter: ";
	
	public static final String PASSPHRASE = "HELLO";

    public static void main(String[] args) throws IOException {
        if (Test.test_shake() == 0) {
            System.out.println("FIPS 202/SHAKE256 Self-Tests OK!");
        }
        Test.test_cshake256();
        Test.test_kmacxof256();
       
        //String enc = Part4.encrypt_public_key("doseon kim", new BigInteger(public_hex, 16));
        /*Part3.generate_key_pair("test");
       
        BigInteger pk_x = new BigInteger("1786809195834544992531552625415921588753052165893517486576193572575579846804266712741676196639621651803122494070269213479238747552723481188673412143480793455");
        BigInteger pk_y = new BigInteger("5491406845570931661325960199719625282179988627608682193642176813472048617409894797587157292417823701092031880143115157168807218699475574568673812334906447462");
        EdwardPoint pk = new EdwardPoint(pk_x, pk_y);*/
        //String enc = Part4.encrypt_public_key("i can't wait till friday hahahoho", pk);
        
        //Part5.sig("doseon kim", "test");
        
        //System.out.println(enc);
        user_prompt();
	
		
		char c = (char) System.in.read();
    }
    
    /**
	 * Initiate the user input encryption. Read the input from user and run the keccak hash function.
	 * @throws IOException Exception in case error with user input text.
	 */
	private static void do_text_input_hash() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String input = in.nextLine();	
		
		System.out.println("Hashed value: " + Part1.KMAC_hash(input));
		System.out.println();
		user_prompt();
	}
	
	/**
	 * Initiate the file encryption. Read the file at given location and run the keccak hash function.
	 * @throws IOException Exception in case file cannot be found.
	 */
	private static void do_file_hash() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		String data = Util.read_file(file_location);
		if (!data.isEmpty()) 
			System.out.println("Hashed value: " + Part1.KMAC_hash(data));
		
		System.out.println();
		user_prompt();
	}
	
	private static void do_sym_encrypt() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("What is your passphrase?:");
		in.nextLine(); // Throw away line.
		String passphrase = in.next();
		
		System.out.println("Save encrypted file location and name?:");
		in.nextLine(); // Throw away line.
		String output = in.next();
		
		String data = Util.read_file(file_location);
		if (!data.isEmpty()) {
            String c_text = Part2.encrypt_message_sym(data, passphrase);
            Files.write(Paths.get(output), c_text.getBytes());
        } 	
		System.out.println();
		user_prompt();
	}
	
	private static void do_sym_decrypt() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("Save decrypted file location and name?:");
		in.nextLine(); // Throw away line.
		String output = in.next();
		
		System.out.println("What is your generated Z?:");
		in.nextLine(); // Throw away line.
		String random_z = in.next();
		
		System.out.println("What is your passphrase?:");
		in.nextLine(); // Throw away line.
		String passphrase = in.next();
		
		String data = Util.read_file(file_location);
		if (!data.isEmpty()) {
            String c_text = Part2.decrypt_message_sym(new BigInteger(random_z), data, passphrase);
            Files.write(Paths.get(output), c_text.getBytes());
        } 	
		System.out.println();
		user_prompt();
	}
    
	private static void create_key_pair() throws IOException {
		Scanner in = new Scanner(System.in);

		System.out.println("Save public key file location and name?:");
		in.nextLine(); // Throw away line.
		String output = in.next();
		
		System.out.println("What is your passphrase?:");
		in.nextLine(); // Throw away line.
		String pw = in.next();

        EdwardPoint pk = Part3.generate_key_pair(pw);
        String save = pk.x + "\n" + pk.y;
        Files.write(Paths.get(output), save.getBytes());
        
		System.out.println();
		user_prompt();
	}
	
	private static void encrypt_public_key() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("Public Key File Location?:");
		in.nextLine(); // Throw away line.
		String pk_file_location = in.next();
		
		System.out.println("Save encrypted file location?:");
		in.nextLine(); // Throw away line.
		String output = in.next();
		
		String pk_data = Util.read_file(pk_file_location);
		String data = Util.read_file(file_location);
		if (!pk_data.isEmpty() && !data.isEmpty()) {
			String lines[] = pk_data.split("\\r?\\n");
			String x = lines[0];
			String y = lines[1];
			
			EdwardPoint V = new EdwardPoint(new BigInteger(x), new BigInteger(y));
			
            String c_text = Part4.encrypt_public_key(data, V);
            Files.write(Paths.get(output), c_text.getBytes());
        } 	
		System.out.println();
		user_prompt();
	}
	
	private static void decrypt_public_key() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("Z.x cryptogram?:");
		in.nextLine(); // Throw away line.
		String Z_x = in.next();
		
		System.out.println("Z.y cryptogram?:");
		in.nextLine(); // Throw away line.
		String Z_y = in.next();
		
		
		System.out.println("Save decrypted file location?:");
		in.nextLine(); // Throw away line.
		String output = in.next();

		String data = Util.read_file(file_location);
		if (!data.isEmpty()) {
			EdwardPoint Z = new EdwardPoint(new BigInteger(Z_x), new BigInteger(Z_y));
			
            String c_text = Part4.decrypt_public_key(data, Z);
            Files.write(Paths.get(output), c_text.getBytes());
        } 	
		System.out.println();
		user_prompt();
	}
	
	private static void sign_file() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println(FILE_INPUT_MESSAGE);
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("Save signature file location?:");
		in.nextLine(); // Throw away line.
		String output = in.next();
		
		System.out.println("passphrase?:");
		in.nextLine(); // Throw away line.
		String pw = in.next();
		
		String data = Util.read_file(file_location);
		if (!data.isEmpty()) {
			
            String c_text = Part5.sig(data, pw);
            Files.write(Paths.get(output), c_text.getBytes());
        } 	
		System.out.println();
		user_prompt();
	}
	
	private static void verify() throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("Original File location?: ");
		in.nextLine(); // Throw away line.
		String file_location = in.next();
		
		System.out.println("Signed File location?: ");
		in.nextLine(); // Throw away line.
		String file_location_signed = in.next();
		
		System.out.println("Public Key File Location?:");
		in.nextLine(); // Throw away line.
		String pk_file_location = in.next();
		
		System.out.println("Z cryptogram?:");
		in.nextLine(); // Throw away line.
		String z_str = in.next();
		
		String pk_data = Util.read_file(pk_file_location);
		String data_ori = Util.read_file(file_location);
		String h = Util.read_file(file_location_signed);
		if (!pk_data.isEmpty() && !data_ori.isEmpty() && !h.isEmpty()) {
			String lines[] = pk_data.split("\\r?\\n");
			String x = lines[0];
			String y = lines[1];
			
			EdwardPoint V = new EdwardPoint(new BigInteger(x), new BigInteger(y));
			BigInteger Z = new BigInteger(z_str);
			BigInteger H = new BigInteger(h, 16);
			
            String c_text = Part5.verify(H, Z, V, data_ori);
            
            System.out.println("H: " + h);
            System.out.println("H': " + c_text);
            
        } 	
		System.out.println();
		user_prompt();
	}
	
    /**
	 * User prompt, displays the available options and reacts corresponding to the response.
	 * @throws IOException Exception in case error with file retrieval or option retrieval.
	 */
	private static void user_prompt() throws IOException {
		System.out.println(WELCOME_MESSAGE);
		for (String s : OPTIONS) {
			System.out.println(s);
		}
		
		char c = (char) System.in.read();
		
		switch (c) {
		case '0': 
			System.exit(0);
			break;
		case '1':
			do_file_hash();
			break;
		case '2': 
			do_text_input_hash();
			break;
		case '3':
			do_sym_encrypt();
		case '4':
			do_sym_decrypt();
		case '5':
			create_key_pair();
		case '6':
			encrypt_public_key();
		case '7':
			decrypt_public_key();	
		case '8':
			sign_file();	
		case '9':
			verify();
		default:
			user_prompt();
		}
	}
 
}
