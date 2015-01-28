package main.java;

import javax.crypto.BadPaddingException;

public interface Authority {
	
	//Recibe la id de la votaci�n, crea las claves y las guarda en BD.
	boolean postKey(String id);
	
	//Recibe la id de la votaci�n y devuelve su clave p�blica para poder cifrar.
	String getPublicKey(String id);
	
	//Recibe la id de la votaci�n y devuelve su clave privada para poder descifrar.
	String getPrivateKey(String id);
	
	//Recibe un voto cifrado y un id de la votaci�n, y comprueba si ese voto ha sido alterado.
	boolean checkVote(byte[] votoCifrado, String id);
	
	//Encripta el texto con la clave p�blica de la votaci�n cuya id se pasa como par�metro.
	byte[] encrypt(String idVote,String textToEncypt);
	
	//Desencripta el texto con la clave privada de la votaci�n cuya id se pasa como par�metro.	
	String decrypt(String idVote,byte[] cipherText) throws BadPaddingException;

}
