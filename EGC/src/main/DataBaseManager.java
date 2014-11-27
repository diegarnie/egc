package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class DataBaseManager {

	private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
	public Collection<String> getVoteFromDataBase(String id) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "jdbc:mysql://localhost:3306/keysvotes";
        String user = "admin";
        String password = "admin";
        String id2="a1";
        
        Collection<String> result = null;
        
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT publicKey, privateKey FROM KeysVotes  WHERE idVotation = '"+id2+"'");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } 
        
        return result;
    }

	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			String url = "jdbc:mysql://localhost:3306/keysvotes";
	        String user = "admin";
	        String password = "admin";
	        con = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException|SQLException e) {
			
			e.printStackTrace();
		}
		
		return con;
	}
	
	public String getPublicKey(String id) {

		String res = null;
        try {
        	Connection con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT publicKey FROM KeysVotes  WHERE idVotation = '"+id+"'");

            if (rs.next()) {
                res = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } 
        
        return res;
    }
	
	public String getPrivateKey(String id) {

		String res = null;
        try {
        	Connection con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT privateKey FROM KeysVotes  WHERE idVotation = '"+id+"'");

            if (rs.next()) {
                res = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } 
        
        return res;
    }
	public String saveKeys(String id, String publicKey, String privateKey) {

		String res = null;
        try {
        	Connection con = getConnection();
            st = con.createStatement();
            st.execute("Insert into keysVotes Values('"+id+"', '" + publicKey + "', '" + privateKey + "' ) ");
            

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } 
        
        return res;
    }
	
}