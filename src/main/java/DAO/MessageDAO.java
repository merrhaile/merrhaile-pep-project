package DAO;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

/*
 * In this layer, the class utilizes JDBC classes to interact with a database and persist data
 * ConnectionUtil custom class handles the connection with the database
 * Among others PreparedStatement and ResultSet are commonly used to execute query and to 
 * retreive results
 */
public class MessageDAO {
    
    /*
     * Persists a new message in the database
     */
    public Message createMessage(Message message){
        Connection con = ConnectionUtil.getConnection();
        try {
            String sgl = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sgl, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int message_id = (int) rs.getLong("message_id");
                // int posted_by = (int) rs.getLong("posted_by");
                // String text = rs.getString("message_text");
                // long time_posted_epoch = rs.getLong("time_posted_epoch");
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Retrieves all messages from the database
     */
    public List<Message> retrieveAllMessages(){
        Connection con = ConnectionUtil.getConnection();
        List<Message> list = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message();
                message.setMessage_id((int)rs.getLong(1));
                message.setPosted_by((int) rs.getLong(2));
                message.setMessage_text(rs.getString(3));
                message.setTime_posted_epoch(rs.getLong(4));
                list.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /*
     * Retrieve a message by message_id
     */
    public Message retrieveMessageByMessageId(int message_id){
        Connection con = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Message message = new Message();
                message.setMessage_id((int)rs.getLong(1));
                message.setPosted_by((int) rs.getLong(2));
                message.setMessage_text(rs.getString(3));
                message.setTime_posted_epoch(rs.getLong(4));
                return message;
            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Deletes message by message_id
     */
    public boolean deleteMessageByMessageId(int message_id){
        Connection con = ConnectionUtil.getConnection();
        boolean result =false;
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, message_id);
            int update = ps.executeUpdate();
           if(update == 1) result = true;
           

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /*
     * Updates a message by message_id with an input text
     */
    public boolean updateMessageByMessageId(int message_id, String text){
        Connection con = ConnectionUtil.getConnection();
        boolean result =false;
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, text);
            ps.setInt(2, message_id);
            int update = ps.executeUpdate();
           if(update == 1) result = true;
           

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /*
     * Retrieves all messages that shares the same account_id
     */
    public List<Message> retrieveAllMessagesForUser(int account_id){
        Connection con =  ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM account " +
                         "INNER JOIN message ON account.account_id = message.posted_by WHERE account_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message();
                message.setMessage_id((int)rs.getLong(1));
                message.setPosted_by((int) rs.getLong(2));
                message.setMessage_text(rs.getString(3));
                message.setTime_posted_epoch(rs.getLong(4));
                messages.add(message);
            }    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    

    
}
