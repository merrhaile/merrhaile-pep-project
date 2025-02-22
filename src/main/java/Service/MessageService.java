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
}
