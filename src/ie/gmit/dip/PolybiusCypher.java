package ie.gmit.dip;

import java.io.*;
import java.util.Arrays;

public class PolybiusCypher {
	
	private static char[][] polybius = {
		{' ','A','D','F','G','V','X'},
		{'A','P','H','0','Q','G','6'},
		{'D','4','M','E','A','1','Y'},
		{'F','L','2','N','O','F','D'},
		{'G','X','K','R','3','C','V'},
		{'V','S','5','Z','W','7','B'},
		{'X','J','9','U','T','I','8'},
	};
	
	public File encryptFile (File message, String key) throws Exception{
		File temp = new File(message.getName() + ".enc");
		PrintWriter fw = new PrintWriter(temp);
		StringBuffer buffer = new StringBuffer();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(message)));
		String line = null;

		while((line = in.readLine()) !=null){
			String ucaseLine = line.toUpperCase();
			buffer.append(ucaseLine);
		}

		String mess = encrypt(buffer.toString(),key);
		fw.println(mess);
		fw.close();
		in.close();
		return temp;
	}
	
	public File decryptFile (File message, String key) throws Exception{
		File temp = new File("decrypted"+message.getName());
		PrintWriter fw = new PrintWriter(temp);
		StringBuffer buffer = new StringBuffer();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(message)));
		String line = null;

		while((line = in.readLine()) !=null){
			String ucaseLine = line.toUpperCase();
			buffer.append(ucaseLine);
		}
		String mess = decrypt(buffer.toString(),key);
		fw.println(mess);
		fw.close();
		in.close();
		return temp;
	}
	
	
	
	public String encrypt (String a, String b){
		String adfgvx = phaseOne(a);
		char[][] polybuisArray = phaseTwo(adfgvx, b);
		String code = phaseThree(polybuisArray,adfgvx, b);
				
		return code;
	}
	
	
	
	private static String phaseOne (String plain){
		// This returns the message after going through the first stage of encryption
		StringBuffer buffer = new StringBuffer();
		char[] plainArray = plain.toCharArray();
		for (int i=0;i < plainArray.length; i++){
			if(plainArray[i]==' '){
				buffer.append("  "); // Allows spaces to be kept in the to the decrypted message.
			}else{	
				for(int row=1; row < polybius.length; row ++){
					for(int col=1; col < polybius[row].length; col ++){
						if(plainArray[i]==polybius[row][col]){
							buffer.append(polybius[row][0]); // Sets the first value for the character cypher
							buffer.append(polybius[0][col]); // Sets the second value for the character cypher
						}					
					}
				}
			}
		}	
		
		return buffer.toString();
	}
	
	private static char[][] phaseTwo(String code, String k){
		// Converts the first stage code into an array
		char[] codeArray = code.toCharArray();
		// Number of columns in the array
		int numCols = k.length();
		int numRows = numRows(code,k);
		char[][] array = new char[numRows][numCols];
		// Ensures that array is symmetrical
		for(int x=0;x<array.length;x++){
			for(int y=0;y<array[x].length;y++){
				array[x][y]=' ';
			}
		}
		int row = 0;
		int col = 0;
		
		for (int i=0;i < codeArray.length; i++){
			array[row][col] = codeArray[i];
			col ++;
			if(col == k.length()){
				col=0;
				row ++;
			}			
		}
		return array;
	}
	
	private static String phaseThree(char[][] columnarTransposition,String code, String k){
		//Performs a columnar transposition on the array made in phaseTwo
		StringBuffer buffer = new StringBuffer();
		char tKey[]=new char [k.length()];
		char[] tn = new char [k.length()];
		int numCols = k.length();
		int numRows = numRows(code,k);
		char[][] encryptedArray= new char[numCols][numRows];
		for (int i=0;i<numRows;i++){ 
			tKey=k.toCharArray();
			//tKey is the key sorted alphabetically
			Arrays.sort(tKey);
			tn = tKey;
			// To compare the the sorted Key with the key
			// For char in the key
			for (int j=0;j<numCols;j++){ 
				int pos=0;
				// To get the position of key.charAt(i) from sorted key
				for (pos=0;pos<tKey.length;pos++){ 
					if (k.charAt(j)==tn[pos]){
						tn[pos]=' ';// Blanks characters already sorted, allows for duplicate characters in the key
					// To break the for loop once the key is found
					break;
					}
				}
			encryptedArray[(numCols-1)-j][i] = columnarTransposition[i][pos];
			}
		}

		
		for(int n= (numCols-1); n>=0; n--){
			for(int m=0; m<numRows; m ++){
				buffer.append(encryptedArray[n][m]);
			}
		}
		return buffer.toString(); // Returns the final encryption
	}
	
	private static int numRows(String c, String d){
		// Calculates the numbers of rows for the array
		int numRow;
		//Converts the first stage encryption key string to char array
				// Calculates the amount of rows required
		if((c.length()) % d.length() ==0){
			numRow = (c.length())/d.length();
		}else{
			numRow = (c.length())/d.length() + 1;
		}
		
		return numRow;
	}	
	
	public String decrypt(String code, String key){
		char [][] codeArray = decryptPh1(code,key);
		String sorted = decryptPh2(codeArray, key);
		String omessage = decryptPh3(sorted);
					
		return omessage;
	}

	private static char [][] decryptPh1(String a, String b){
		// Converts the cypher string to a 2D array to begin decryption
		char [] eMArray = a.toCharArray();
		int collen = a.length()/b.length();
		int rowlen = b.length();
		char [][] eArray= new char [collen][rowlen];
		int row=0;
		int col=0;
		for (int i=0;i < eMArray.length; i++){ // populates the 2D Array
			eArray[row][col] = eMArray[i];
			row ++;
			if(row == collen){
				row=0;
				col ++;
			}			
		}
		return eArray;
	}
	
	private static String decryptPh2(char[][] s, String key){
		// Undoes the columnar transposition on the cypher
		char [] tx = new char [key.length()];
		char [] tn = new char [key.length()];
		char [][] sortArray = new char [s.length][key.length()];
		tx=key.toCharArray();
		Arrays.sort(tx);
		for (int i=0;i<s.length;i++){ 
			tn=key.toCharArray();
			// To compare the the sorted Key with the key
			// For char in the key
			for (int j=(s[i].length-1);j>=0;j--){ 
				int pos=0;
				// To get the position of key.charAt(i) from sorted key
				for (pos=tx.length-1;pos>=0;pos--){ 
					if (tx[j]==tn[pos]){ 
						tn[pos]=' ';// Allows for repeated characters in the key
						// To break the for loop once the key is found
						break;
					}
				}
				sortArray[i][j] = s[i][pos];
			}
			
		}

		StringBuffer returnMessage = new StringBuffer();
		for(int u=0; u < sortArray.length; u ++){// Converts the array to a string
			for(int v = 0 ; v<sortArray[u].length;v++){
				returnMessage.append(sortArray[u][v]);

			}
		}
		return returnMessage.toString();
	}

	private static String decryptPh3(String e){
		// returns the final decrypted message
		char [] rM = e.toCharArray();

		StringBuffer mess = new StringBuffer();
		for(int l = 0 ; l<rM.length; l += 2){
			if(rM[l]==' '){
				mess.append(' ');	
			}else{
			int x = 0;
			for(; x < polybius.length; x++) {
				if(rM[l] == polybius[x][0]){
					break;
			}
			}
			
			// found the y coordinate in the grid
			int y = 0;
			for(; y < polybius.length; y++) {
				if(rM[l+1] == polybius[y][0])
					break;
			}
			// assign the value from the grid
			mess.append(polybius[x][y]);


		}
		}
		
		return mess.toString(); // the original message
	}
	
}
