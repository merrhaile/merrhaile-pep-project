package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    public Message createMessage(Message message){
        Integer user_id = message.getPosted_by();
        Account user =  accountDAO.userLogin(new Account(user_id, null, null));
        if(user == null) return null;

        if(message.getMessage_text().length() == 0) return null;
        if(message.getMessage_text().length() > 255) return null;
        
        return messageDAO.createMessage(message);
    }
    public List<Message> retrieveAllMessages(){
        return messageDAO.retrieveAllMessages();

    }

    public Message retrieveMessageByMessageId(int message_id){
        return messageDAO.retrieveMessageByMessageId(message_id);
    }

    public Message deleteMessageByMessageId(int message_id){
        Message message = retrieveMessageByMessageId(message_id);
        boolean result = messageDAO.deleteMessageByMessageId(message_id);
        if(message != null && result == true) return message;
        else return null;
    }

    public Message updateMessageByMessageId(int message_id, String text){
        if(text.length() == 0 || text.length() > 255 ) return null;
        
        Message message = retrieveMessageByMessageId(message_id);
        if(message != null) {
            boolean result =  messageDAO.updateMessageByMessageId(message_id, text);
            if(result == true) return retrieveMessageByMessageId(message_id);
            else return null;
        } else return null;
       
    }

    public void retrieveAllMessagesForUser(){

    }
}
