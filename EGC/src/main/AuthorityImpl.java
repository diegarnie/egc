package main;

import java.security.KeyPair;

import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import Decoder.BASE64Encoder;




public class AuthorityImpl implements Authority{

	private static HashMap<String, KeyPair> database = new HashMap<String, KeyPair>();
	
	public boolean postKey(String id) {
		
		try {

			
			 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			
			 SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
			 keyGen.initialize(1024, random);
			
			 KeyPair pair = keyGen.generateKeyPair();
			
			 database.put(id, pair);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return false;
	}

	
	public String getPublicKey(String id) {
		KeyPair pair = database.get(id);
		BASE64Encoder encoder = new BASE64Encoder();
		
		return encoder.encode((pair.getPublic().getEncoded()));
	}

	
	public String getPrivateKey(String id) {

		KeyPair pair = database.get(id);
		BASE64Encoder encoder = new BASE64Encoder();
		
		
		return encoder.encode((pair.getPrivate().getEncoded()));
	}

	
	public boolean checkVote(String votoCifrado, String id) {

		
		//Get the key
		String key = getPublicKey(id);
		
		return false;

	}

}
