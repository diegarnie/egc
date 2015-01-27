package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RemoteDataBaseManager {
	public boolean postKeys(String id, String publicKey,String privateKey ){
		boolean success = false;
		try {
			
			id = URLEncoder.encode(id, "UTF-8");
			publicKey = URLEncoder.encode(publicKey, "UTF-8");
			privateKey = URLEncoder.encode(privateKey, "UTF-8");
	        URL url;
			
			url = new URL("http://egcprueba.esy.es/default2.php");
			
	        URLConnection connection = url.openConnection();
	        connection.setDoOutput(true);
	
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	        
	        out.write("id=" + id+"&");
	        out.write("pub=" + publicKey+"&");
	        out.write("priv=" + privateKey);
	        out.close();
	
	        BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
	        String decodedString;
	        String fullText="";
	        while ((decodedString = in.readLine()) != null) {
	        	fullText+=decodedString;
	        }
	        in.close();
	        success = fullText.contains("New record created successfully");
	        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	public boolean postAESKey(String id, String secretKey){
		boolean success = false;
		try {
			
			id = URLEncoder.encode(id, "UTF-8");
			secretKey = URLEncoder.encode(secretKey, "UTF-8");
	        URL url;
			
			url = new URL("http://egcprueba.esy.es/AESdefault2.php");
			
	        URLConnection connection = url.openConnection();
	        connection.setDoOutput(true);
	
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	        
	        out.write("id=" + id+"&");
	        out.write("secretKey=" + secretKey);
	        out.close();
	
	        BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
	        String decodedString;
	        String fullText="";
	        while ((decodedString = in.readLine()) != null) {
	        	fullText+=decodedString;
	        }
	        in.close();
	        success = fullText.contains("New record created successfully");
	        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	public String readPage(String id, String method){
		BufferedReader in = null;
		URL url = null;
		String linea;
		String textoPagina="";

		try{
			if (method == "AES"){
				url = new URL("http://egcprueba.esy.es/AESdefault.php?id="+id);
			}
			else{
				url = new URL("http://egcprueba.esy.es/default.php?id="+id);
			}
			
		}
		catch (MalformedURLException e){
			
			e.printStackTrace();
		}
		
				
		try{
			
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		}
		catch(IOException e){
			
			e.printStackTrace();
		}
			
		try{
			while ((linea = in.readLine()) != null) {
			     textoPagina = textoPagina + linea;
			}
		}
		catch(IOException e){
			
			e.printStackTrace();
		}
		
		
		return textoPagina;
	}
	
	public String getSecretKey(String id){
		String fullPage = readPage(id,"AES");
		String res = "";
		
		for(int j = fullPage.indexOf("Secretkey:") + 10; fullPage.charAt(j)!='<' && j< fullPage.length() ;j++){
			
			res += fullPage.charAt(j);
		}
		
		return res;
	}
	
	public String getPublicKey(String id){
		String fullPage = readPage(id,"RSA");
		String res = "";
		
		for(int j = fullPage.indexOf("Publickey: ") + 10; fullPage.charAt(j)!='<' && j< fullPage.length() ;j++){
			
			res += fullPage.charAt(j);
		}
		
		return res;
	}
	
	public String getPrivateKey(String id){
		String fullPage = readPage(id,"RSA");
		String res = "";
		
		for(int j = fullPage.indexOf("Privatekey: ") + 11; fullPage.charAt(j)!='<' && j< fullPage.length() ;j++){
			
			res += fullPage.charAt(j);
		}
		
		return res;
	}

}
