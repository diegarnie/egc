package main.java;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AuthorityImplAES{

	/**
	 * Este m�todo es usado para crear una clave de cifrado AES de 256 bits y almacenarla
	 * en una base de datos en Hostinger. El m�todo crear� la clave siempre
	 * que no exista ya una entrada en la base de datos para la misma votaci�n.
	 * @param id. El par�metro id se refiere a la id de la votaci�n a la que se le asociar�
	 * la clave.
	 * @return success. Boolean que indica si la operaci�n ha tenido �xito.
	 */
	public boolean postKey(String id) {
		boolean success = false;
		try {

			
			KeyGenerator key = KeyGenerator.getInstance("AES"); 
			key.init(256); 
			//Generamos la clave de 256 bits
			SecretKey secretKey = key.generateKey(); 
			
			//Convertimos la clave a Base64. 
			String secretKeyString = DatatypeConverter.printBase64Binary(secretKey.getEncoded());
			 
			//Llamamos al m�todo para almacenar la clave de cifrado en la base de datos
			// de hostinger.
			RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			if (rdbm.postAESKey(id, secretKeyString)){
				success = true;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
		return success;
	}
	
	/**
	 * M�todo que obtiene la clave de cifrado AES asociada a una votaci�n
	 * @param id La id de la votaci�n cuya clave desea conocerse
	 * @return La clave de cifrado asociada a la votaci�n
	 */
	public String getSecretKey(String id) {
		RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
		
		//Llamamos al m�todo que conecta con la base de datos remota y devuelve
		// el valor de la clave asociada a la votaci�n.
		return rdbm.getSecretKey(id);
	}

	/**
	 * M�todo que comprueba que un voto cifrado mediante AES no ha sido modificado.
	 * Para ello, es necesario que antes de cifrar dicho voto, se le a�ada al final del voto
	 * el resultado de calcula su c�digo Hash mediante el algoritmo md5.
	 * @param votoCifrado El voto que se quiere comprobar si ha sido modificado o no. Debe incluir
	 * el su c�digo hash al final calculado mediante el algoritmo md5.
	 * @param id El id de la votaci�n para obtener la clave para descifrar
	 * @return
	 */
	public boolean checkVote(byte[] votoCifrado, String id) {
		// TODO Auto-generated method stub
		AuthorityImplAES authority = new AuthorityImplAES();
		//Obtenemos la clave de la base de datos
		String secretKey = authority.getSecretKey(id);
		Boolean res = null;
		   
		SecretKeySpec key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey), "AES");
	  	Cipher cipher;
	  	try {
	  		cipher = Cipher.getInstance("AES");
		    
	  		cipher.init(Cipher.DECRYPT_MODE, key);
	  		//Obtenemos el resultado de descifrar el array de byte que nos llega como par�metro
	  		byte[] resByte = cipher.doFinal(votoCifrado);
	  		
	  		//Creamos dos nuevos arrays, uno para almacenar el hash y otro para el mensaje original
	  		byte[] hash = new byte[16];
	  		byte[] originalText = new byte[resByte.length - hash.length];
	  		
	  		//Copiamos en la variable 'originalText' el mensaje original y en la 
	  		// variable 'hash' el codigo hash del mensaje, que corresponde a los 
	  		// �ltimos 16 bytes
	  		System.arraycopy(resByte, 0, originalText, 0, originalText.length);
	  		System.arraycopy(resByte, originalText.length, hash, 0, hash.length);
	  		
	  		//Obtenemos el c�digo hash de la variable 'originalText'
	  		MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			byte[] newHash = md.digest(originalText);
			
			//Comparamos que el hash reci�n calculado coincida con el 
			// que ten�amos previamente
			res = Arrays.equals(hash, newHash);
		 

	  	}
	  	catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * M�todo que cifra un texto usando el algoritmo AES y la clave de cifrado asociada a 
	 * la votaci�n cuyo par�metro se recibe. Tambi�n presenta la opci�n de a�adir
	 * el c�digo hash del texto justo detr�s antes de cifrar.
	 * @param idVote La id de la votaci�n cuya clave se quiere usar para el cifrado
	 * @param textToEncypt Texto a cifrar
	 * @param addHashCode Si toma el valor 'True' se a�adir� tras el texto original
	 * su c�digo hash calculado mediante el algoritmo md5
	 * @return
	 */
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
	  		//Si est� activada la opci�n de a�adir hash al final, calculamos el hash 
	  		//para a�adirlo 
	  		if(addHashCode){ 
				MessageDigest md;
				md = MessageDigest.getInstance("MD5");
				byte[] theDigest = md.digest(bytesOfMessage);
				
				//A�adimos al final del texto el hash creado
				bytesToEcrypt = new byte[bytesOfMessage.length+theDigest.length];
		  		System.arraycopy(bytesOfMessage, 0, bytesToEcrypt, 0, bytesOfMessage.length);
		  		System.arraycopy(theDigest, 0, bytesToEcrypt, bytesOfMessage.length, theDigest.length);
		  		
				
	  		}
	  		//Si no est� activada la opcti�n de a�adir el hash, ciframos el texto
	  		// original tal y como se recibe.
	  		else{
	  			bytesToEcrypt = bytesOfMessage;
	  		}
	  		cipher = Cipher.getInstance("AES");
	  		
		    
	  		//Se realiza el cifrado
	  		cipher.init(Cipher.ENCRYPT_MODE, key);
	  		res = cipher.doFinal(bytesToEcrypt);
		 

	  	}
	  	catch (Exception e) {
	  		e.printStackTrace();
		}
		
		return res;
	}

	/**
	 * M�todo que descifra un texto usando el algoritmo AES y la clave de cifrado asociada
	 * a la votaci�n que se recibe como par�metro. Tambi�n retir� el c�digo hash de la parte
	 * final del mensaje en el caso de que se le indique.
	 * @param idVote La id de la votaci�n cuya clave desea usarse para descifrar.
	 * @param cipherText El texto que se desea descifrar
	 * @param hashAdded Si toma el valor 'True', indica que el mensaje tiene en la parte final
	 * su c�digo hash de 16 bytes, el cual deber� ser eliminado
	 * @return El texto descifrado
	 */
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
	  		
	  		//Si se a�adi� un codigo hash para comprobaciones lo descartamos
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
