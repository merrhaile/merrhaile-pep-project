package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

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
            if(rs.next()){
                int account_id = (int) rs.getLong(1);
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
            String sql = "SELECT username, password FROM account WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Account(rs.getString("username"), rs.getString("password")) ;     
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
