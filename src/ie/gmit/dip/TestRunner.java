package ie.gmit.dip;

import java.io.File;
import java.util.Scanner;

public class TestRunner {
	private static PolybiusCypher pb = new PolybiusCypher();
	
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		Scanner t = new Scanner(System.in);
		System.out.println("Choose 1 to encrypt a message.");
		System.out.println("Choose 2 to decrypt a cypher.");
		System.out.println("Choose 3 to encrypt a file.");
		System.out.println("Choose 4 to decrypt a file.");
		
		int option = s.nextInt();
		if(option == 1){//encrypt a string
			System.out.println("Enter text to be encyrpted");
			String message = t.nextLine().toUpperCase();// Takes in message and changes it to upper case
			System.out.println("Enter key: One word.");
			//Encryption key for rearranging the array
			String key = s.next().toUpperCase();// Takes in message and changes it to upper case
			String encryptedMessage = pb.encrypt(message,key);
			System.out.println(encryptedMessage);
		}else if (option == 2){
			System.out.println("Enter text to be decyrpted");
			String cypher = t.nextLine().toUpperCase();// Takes in message and changes it to upper case
			System.out.println("Enter key: One word.");
			//Encryption key for rearranging the array
			String key = s.next().toUpperCase();// Takes in message and changes it to upper case
			String decryptedMessage = pb.decrypt(cypher,key);
			System.out.println(decryptedMessage);
		}else if (option == 3){
			System.out.println("Enter name of file to be encyrpted");
			String file = t.nextLine().toUpperCase();// Takes the file name
			File message = new File("./"+file);
			System.out.println("Enter key: One word.");
			//Encryption key for rearranging the array
			String key = s.next().toUpperCase();// Takes in message and changes it to upper case
			File enc = pb.encryptFile(message,key);// Takes in a file and key, encrypts and outputs its to a file
			System.out.println("File encrypted");
		}else if (option == 4){
			System.out.println("Enter name of file to be decyrpted");
			String file = t.nextLine().toUpperCase();// Takes the file name
			File cypher = new File("./" + file);
			System.out.println("Enter key: One word.");
			//Encryption key for rearranging the array
			String key = s.next().toUpperCase();// Takes in message and changes it to upper case
			File message = pb.decryptFile(cypher,key);// Takes in a file and key, decrypts and outputs its to a file
			System.out.println("File decrypted");
		}
		
		
		t.close();
		s.close();
	}
}

