package main.java;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AuthorityImplAES{

	
	public boolean postKey(String id) {
		boolean success = false;
		try {

			
			KeyGenerator key = KeyGenerator.getInstance("AES"); 
			key.init(256); 
			SecretKey secretKey = key.generateKey(); 
			
			 
			 String secretKeyString = DatatypeConverter.printBase64Binary(secretKey.getEncoded());
			 
			 
			 RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			 if (rdbm.postAESKey(id, secretKeyString)){
				 success = true;
			 }
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return success;
	}

	public String getSecretKey(String id) {
		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		
		return rdbm.getSecretKey(id);
	}

	public boolean checkVote(byte[] votoCifrado, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public byte[] encrypt(String idVote, String textToEncypt,Boolean addHashCode) {
		AuthorityImplAES authority = new AuthorityImplAES();
		String secretKey = authority.getSecretKey(idVote);
		byte[] res = null;
		byte[] bytesToEcrypt = null;
		byte[] bytesOfMessage = null;
		SecretKeySpec key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey), "AES");
	  	Cipher cipher;
	  	try {
	  		
	  		bytesOfMessage = textToEncypt.getBytes("UTF-8");
	  		//Si está activada la opción de añadir hash al final, calculamos el hash 
	  		//para añadirlo 
	  		if(addHashCode){ 
				MessageDigest md;
				md = MessageDigest.getInstance("MD5");
				byte[] theDigest = md.digest(bytesOfMessage);
				
				//Añadimos al final del texto el hash creado
				bytesToEcrypt = new byte[bytesOfMessage.length+theDigest.length];
		  		System.arraycopy(bytesOfMessage, 0, bytesToEcrypt, 0, bytesOfMessage.length);
		  		System.arraycopy(theDigest, 0, bytesToEcrypt, bytesOfMessage.length, theDigest.length);
		  		
				
	  		}
	  		else{
	  			bytesToEcrypt = bytesOfMessage;
	  		}
	  		cipher = Cipher.getInstance("AES");
	  		
		    
	  		
	  		cipher.init(Cipher.ENCRYPT_MODE, key);
	  		res = cipher.doFinal(bytesToEcrypt);
		 

	  	}
	  	catch (Exception e) {
	  		e.printStackTrace();
		}
		
		return res;
	}

	
	public String decrypt(String idVote, byte[] cipherText, Boolean hashAdded){
		
		AuthorityImplAES authority = new AuthorityImplAES();
		String secretKey = authority.getSecretKey(idVote);
		String res = null;
		   
		SecretKeySpec key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey), "AES");
	  	Cipher cipher;
	  	try {
	  		cipher = Cipher.getInstance("AES");
		    
	  		cipher.init(Cipher.DECRYPT_MODE, key);
	  		byte[] resByte = cipher.doFinal(cipherText);
	  		
	  		//Si se añadió un codigo hash para comprobaciones lo descartamos
	  		if (hashAdded){
	  			byte[] originalText = new byte[resByte.length - 16];
	  			System.arraycopy(resByte, 0, originalText, 0, originalText.length);
	  			res = new String (originalText);
	  		}
	  		else{
	  			res = new String(resByte);
	  		}
	  		 
		 

	  	}
	  	catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
		  		  
	}

}
