package main.java;

import javax.crypto.BadPaddingException;

public interface Authority {
	
	//Recibe la id de la votación, crea las claves y las guarda en BD.
	public boolean postKey(String id);
	
	//Recibe la id de la votación y devuelve su clave pública para poder cifrar.
	public String getPublicKey(String id);
	
	//Recibe la id de la votación y devuelve su clave privada para poder descifrar.
	public String getPrivateKey(String id);
	
	//Recibe un voto cifrado y un id de la votación, y comprueba si ese voto ha sido alterado.
	public boolean checkVote(byte[] votoCifrado, String id);
	
	//Encripta el texto con la clave pública de la votación cuya id se pasa como parámetro.
	public byte[] encrypt(String idVote,String textToEncypt);
	
	//Desencripta el texto con la clave privada de la votación cuya id se pasa como parámetro.	
	public String decrypt(String idVote,byte[] cipherText) throws BadPaddingException;

}
