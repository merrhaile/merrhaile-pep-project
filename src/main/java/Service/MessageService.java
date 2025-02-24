package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    /*
     * Default constructor intailizes the DAO instances
     */
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /*
     * Creates a message for a specific user with the help of accountDAO class
     * First it checks if the the user account exist with the given account_id 
     * Then it validates the text message
     */
    public Message createMessage(Message message){
        Integer user_id = message.getPosted_by();
        Account user =  accountDAO.userLogin(new Account(user_id, null, null));
        if(user == null) return null;

        if(message.getMessage_text().length() == 0) return null;
        if(message.getMessage_text().length() > 255) return null;
        
        return messageDAO.createMessage(message);
    }

    /*
     * Invokes retrieveAllMessages from messageDAO
     * Returns list of messages 
     */
    public List<Message> retrieveAllMessages(){
        return messageDAO.retrieveAllMessages();

    }

    /*
     * Invokes retrieveMessageByMessageId from messageDAO class
     * to retrieve a single message by message_id
     * returns the message
     */
    public Message retrieveMessageByMessageId(int message_id){
        return messageDAO.retrieveMessageByMessageId(message_id);
    }

    /*
     * Uses deleteMessageByMessageId from messageDAO class
     * Checks if the message exist in the database
     * Deletes message by message_id
     * returns the message or null
     */
    public Message deleteMessageByMessageId(int message_id){
        Message message = retrieveMessageByMessageId(message_id);
        boolean result = messageDAO.deleteMessageByMessageId(message_id);
        if(message != null && result == true) return message;
        else return null;
    }

    /*
     * it validates the input text against constraints 
     * Updates existing message with an input text if the message is found 
     * and returns the updated version of the message
     * if no message found returns null
     */
    public Message updateMessageByMessageId(int message_id, String text){
        if(text.length() == 0 || text.length() > 255 ) return null;
        
        Message message = retrieveMessageByMessageId(message_id);
        if(message != null) {
            boolean result =  messageDAO.updateMessageByMessageId(message_id, text);
            if(result == true) return retrieveMessageByMessageId(message_id);
            else return null;
        } else return null;
       
    }

    /*
     * Retrieves all the message by a single user and 
     * returns a collection of messages
     */
    public List<Message> retrieveAllMessagesForUser(int account_id){
        return messageDAO.retrieveAllMessagesForUser(account_id);
    }
}
