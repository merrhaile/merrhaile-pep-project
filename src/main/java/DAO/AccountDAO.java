package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

/*
 * In this layer, the class utilizes JDBC classes to interact with a database and persist data
 * ConnectionUtil custom class handles the connection with the database
 * Among others PreparedStatement and ResultSet are commonly used to execute query and to 
 * retreive results
 */
public class AccountDAO {
    
    /*
     * This method will insert a users credentials into a database account
     * It returns an updated account object  with a new account_id
     */
    public Account userRegistration(Account account){
        Connection con = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getPassword());
            ps.setString(2, account.getUsername());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            PreparedStatement ps1 = con.prepareStatement("SELECT * FROM account WHERE username = ?");
            ps1.setString(1, account.getUsername());
            ResultSet rs1 = ps1.executeQuery();
            if(rs.next()){
                int account_id = (int) rs.getLong(1);
                // String username = rs1.getString(2);
                // String password = rs1.getString(3);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * This methods checks if the given credentials exist in the database
     * It returns and Account object if it exist, null if it does not
     */
    public Account userLogin(Account account){
        Connection con = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ? OR account_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setInt(2, account.getAccount_id());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int account_id = (int) rs.getLong(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                return new Account(account_id, username, password) ;     
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
