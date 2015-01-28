package main.java;

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


	/**
	 * Este m�todo es usado para crear un par de claves de cifrado RSA y almacenarlas
	 * en una base de datos en Hostinger. El m�todo crear� el par de claves siempre
	 * que no exista ya una entrada en la base de datos para la misma votaci�n.
	 * @param id. El par�metro id se refiere a la id de la votaci�n a la que se le asociar�
	 * el par de claves.
	 * @return success. Boolean que indica si la operaci�n ha tenido �xito.
	 */
	public boolean postKey(String id) {
		boolean success = false;
		try {

			
			 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			
			 SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
			 keyGen.initialize(2048, random);
			
			 //Generamos el par de claves
			 KeyPair pair = keyGen.generateKeyPair();
			
			 //Convertimos las claves a un String en Base64 para almacenarlas en la BD
			 String publicKey = DatatypeConverter.printBase64Binary(pair.getPublic().getEncoded());
			 String privateKey= DatatypeConverter.printBase64Binary(pair.getPrivate().getEncoded());
			 
			 
			 RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			 //Llamamos al m�todo que se encarga de guardar el par de claves asociadas
			 // a la votaci�n cuya id se especifica como par�metro.
			 if (rdbm.postKeys(id, publicKey, privateKey)){
				 success = true;
			 }
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return success;
	}

	/**
	 * Este m�todo obtiene la clave p�blica RSA de una votaci�n que exista en la base de datos.
	 * @param id. Corresponde al id de la votaci�n
	 * @return String que contiene la clave p�blica
	 */
	public String getPublicKey(String id) {
		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		//Llamamos al m�todo que conecta con la base de datos remota y obtiene la clave p�blica.
		return rdbm.getPublicKey(id);
	}

	/**
	 * Este m�todo obtiene la clave privada RSA de una votaci�n que exista en la base de datos.
	 * @param id. Corresponde al id de la votaci�n
	 * @return String que contiene la clave privada
	 */
	public String getPrivateKey(String id) {

		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		
		//Llamamos al m�todo que conecta con la base de datos remota y obtiene la clave privada.
		return rdbm.getPrivateKey(id);
	}

	/**
	 * M�todo que comprueba que un voto no ha sido modificado.
	 * @param votoCifrado. Corresponde con el voto cifrado mediante el algoritmo RSA
	 * @param id. Corresponde a la id de la votaci�n.
	 * @return boolean que indica si la comprobaci�n ha sido correcta o no. Si 
	 * ha sido correcta se devuelve true y en caso contrario false.
	 */
	public boolean checkVote(byte[] votoCifrado, String id) {
		
		boolean res = true;
		try {
			//Intentamos descifrar el voto y capturamos la excepci�n.
			decrypt(id, votoCifrado);
		} catch (BadPaddingException e) {
			res = false;
		}
		return res;

	}

	/**
	 * M�todo que cifra mediante RSA una cadena de texto con la clave p�blica de la votaci�n 
	 * asociada a la id de votaci�n enviada como par�metro.
	 * @param idVote. La id de la votaci�n cuya clave p�blica queremos usar para cifrar.
	 * @param textToEncrypt. El texto que deseamos cifrar.
	 * @return array de byte que contiene el mensaje cifrado
	 */
	public byte[] encrypt(String idVote,String textToEncypt){
		
		byte[] res = null;
		try {
			Cipher rsa;
			Authority authority = new AuthorityImpl();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			//Construimos el objeto PublicKey a partir de la cadena de la base de datos
			KeySpec keySpec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(authority.getPublicKey(idVote)));
			PublicKey pubKeyFromBytes = keyFactory.generatePublic(keySpec);
			
			rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsa.init(Cipher.ENCRYPT_MODE, pubKeyFromBytes);
	    
			//Realizamos el cifrado
			res = rsa.doFinal(textToEncypt.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			
			e.printStackTrace();
		} 
		
		return res;
	}
	
	/**
	 * M�todo que descifra mediante RSA un array de byte usando la clave privada asociada a la votaci�n
	 * cuyo id se pasa como par�metro. Lanza una excepci�n que nos ayudar� a comprobar si 
	 * el voto ha sido modificado.
	 * @param idVote. La id de la votaci�n cuya clave privada queremos usar para descifrar.
	 * @param cipherText. El texto que deseamos descifrar.
	 * @return cadena de texto que contiene el mensaje original.
	 */
	public String decrypt(String idVote,byte[] cipherText) throws BadPaddingException{
		
		String res = null;
		try {
			Cipher rsa;
			Authority authority = new AuthorityImpl();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			//Construimos el objeto PrivateKey a partir de la cadena de la base de datos
			KeySpec keySpec = new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(authority.getPrivateKey(idVote)));
			PrivateKey privKeyFromBytes = keyFactory.generatePrivate(keySpec);
			
			rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsa.init(Cipher.DECRYPT_MODE, privKeyFromBytes);
			
	    
			//Se procede a descifrar el texto cifrado
			byte[] bytesDesencriptados = rsa.doFinal(cipherText);
		    res = new String(bytesDesencriptados);
		} catch (IllegalBlockSizeException  | InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			
			e.printStackTrace();
		}

		
		return res;
	}
}
