package main.java;


public class Prueba {
	
	public static void main(String[] args) {
		try {
		AuthorityImplAES authorityAES = new AuthorityImplAES();
		
		System.out.println(authorityAES.getSecretKey("1000"));
		
		String prueba = "Cifra y descifra";
		
		byte[] cifrado = authorityAES.encrypt("1000", prueba,true);
		
		System.out.println(authorityAES.checkVote(cifrado, "1000"));
		System.out.println(authorityAES.decrypt("1000", cifrado,true));
		
		Authority authority = new AuthorityImpl();
		
		byte[] cifradoRSA = authority.encrypt("1000", prueba);
		System.out.println(authority.decrypt("1000", cifradoRSA));
		

		
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
	}
}
