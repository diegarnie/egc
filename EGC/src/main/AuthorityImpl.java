package main;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


import javax.xml.bind.DatatypeConverter;




public class AuthorityImpl implements Authority{

	
	public boolean postKey(String id) {
		
		try {

			
			 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			
			 SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
			 keyGen.initialize(1024, random);
			
			 KeyPair pair = keyGen.generateKeyPair();
			
			 
			 String publicKey = DatatypeConverter.printBase64Binary(pair.getPublic().getEncoded());
			 String privateKey= DatatypeConverter.printBase64Binary(pair.getPrivate().getEncoded());
			 
			 
			 DataBaseManager dbm=new DataBaseManager();
			 dbm.saveKeys(id, publicKey, privateKey);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return false;
	}

	
	public String getPublicKey(String id) {
		DataBaseManager dbm=new DataBaseManager();
		
		return dbm.getPublicKey(id);
	}

	
	public String getPrivateKey(String id) {

		DataBaseManager dbm=new DataBaseManager();
		
		
		return dbm.getPrivateKey(id);
	}

	
	public boolean checkVote(String votoCifrado, String id) {

		/*TODO*/
		//Get the key
		//String key = getPublicKey(id);
		
		return false;

	}

}
