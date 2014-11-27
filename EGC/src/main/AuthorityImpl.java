package main;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


import javax.xml.bind.DatatypeConverter;




public class AuthorityImpl implements Authority{

	
	public boolean postKey(String id) {
		boolean success = false;
		try {

			
			 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			
			 SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
			 keyGen.initialize(1024, random);
			
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

	
	public boolean checkVote(String votoCifrado, String id) {

		/*TODO*/
		//Get the key
		//String key = getPublicKey(id);
		
		return false;

	}

}
