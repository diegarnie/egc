package main;

public interface Authority {
	
	//Recibe la id de la votaci�n, crea las claves y las guarda en BD.
	public boolean postKey(String id);
	
	//Recibe la id de la votaci�n y devuelve su clave p�blica para poder cifrar.
	public String getPublicKey(String id);
	
	//Recibe la id de la votaci�n y devuelve su clave privada para poder descifrar.
	public String getPrivateKey(String id);
	
	//Recibe un voto cifrado y un id de la votaci�n, y comprueba si ese voto ha sido alterado.
	public boolean checkVote(String votoCifrado, String id);

}
