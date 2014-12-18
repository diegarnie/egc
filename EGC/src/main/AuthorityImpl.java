package main;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;




public class AuthorityImpl implements Authority{

	
	public boolean postKey(String id) {
		boolean success = false;
		try {

			
			 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			
			 SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
			 keyGen.initialize(2048, random);
			
			 KeyPair pair = keyGen.generateKeyPair();
			
			 
			 String publicKey = DatatypeConverter.printBase64Binary(pair.getPublic().getEncoded());
			 String privateKey= DatatypeConverter.printBase64Binary(pair.getPrivate().getEncoded());
			 
			 
			 RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			 if (rdbm.postKeys(id, publicKey, privateKey)){
				 success = true;
			 }
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return success;
	}

	
	public String getPublicKey(String id) {
		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		
		return rdbm.getPublicKey(id);
	}

	
	public String getPrivateKey(String id) {

		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		
		
		return rdbm.getPrivateKey(id);
	}

	
	public boolean checkVote(byte[] votoCifrado, String id) {
		
		boolean res = true;
		try {
			decrypt(id, votoCifrado);
		} catch (BadPaddingException e) {
			res = false;
		}
		return res;

	}

	
	public byte[] encrypt(String idVote,String textToEncypt){
		
		byte[] res = null;
		try {
			Cipher rsa;
			Authority authority = new AuthorityImpl();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(authority.getPublicKey(idVote)));
			
			PublicKey pubKeyFromBytes = keyFactory.generatePublic(keySpec);
			
			rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsa.init(Cipher.ENCRYPT_MODE, pubKeyFromBytes);
	    
		
			res = rsa.doFinal(textToEncypt.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			
			e.printStackTrace();
		} 
		
		return res;
	}
	
	public String decrypt(String idVote,byte[] cipherText) throws BadPaddingException{
		
		String res = null;
		try {
			Cipher rsa;
			Authority authority = new AuthorityImpl();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(authority.getPrivateKey(idVote)));
			
			PrivateKey privKeyFromBytes = keyFactory.generatePrivate(keySpec);
			
			rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsa.init(Cipher.DECRYPT_MODE, privKeyFromBytes);
			
	    
		
			byte[] bytesDesencriptados = rsa.doFinal(cipherText);
		    res = new String(bytesDesencriptados);
		} catch (IllegalBlockSizeException  | InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			
			e.printStackTrace();
		}

		
		return res;
	}
}
