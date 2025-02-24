package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /*
     * Default constructor intializes the service classees
     */
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::userRegistration);
        app.post("login", this::userLogin);
        app.post("messages", this::createMessage);
        app.get("messages", this::retrieveAllMessages);
        app.get("messages/{message_id}", this::retrieveMessageByMessageId);
        app.delete("messages/{message_id}", this::deleteMessageByMessageId);
        app.patch("messages/{message_id}", this::updateMessageByMessageId);
        app.get("accounts/{account_id}/messages", this::retrieveAllMessagesForUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    /*
     * Handler for user Registration
     * This handler utilizes the userRegistration method from accountService
     * to registor a new user. If the registaration is succesful, it returns
     * new user with an ID attached, status:200. 
     * If not, it returns null staus: 400
     */

    private void userRegistration(Context context)throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);
        Account registeredUser = accountService.userRegistration(account);
        
        if(registeredUser != null) {
            context.json(om.writeValueAsString(registeredUser)).status(200);
        }else{
            context.status(400);
        }
    }


    /*
     * User login handler
     * This handler checks if a given user credentials are valid
     * It returns the user, if the user credentials exist in the database
     * with successfull status: 200
     * It returns null if not status:401
     */

    private void userLogin(Context context) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);
        Account userDetail = accountService.userLogin(account);
        
        if(userDetail != null) {
            context.json(om.writeValueAsString(userDetail)).status(200);
        }
        else{
            context.status(401);
        }
    }
    /*
     * The handler creates a new message by utilizing createMessage method from
     * messageService and pass the request body as an argument.
     * If successful, it returns the created message with status:200
     * if not, it response status; 400
     */

    private void createMessage(Context context) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(context.body(), Message.class);
        
       Message messageDetail = messageService.createMessage(message);
       if(messageDetail != null) {
        context.json(om.writeValueAsString(messageDetail)).status(200);
       }else{
        context.status(400);
       }
    }
    /*
     * This handler uses retrieveAllMessages method from messageService
     * to get all the list of messages.
     * it respondes with status:200 whether the list is empty or not
     */

    private void retrieveAllMessages(Context context) {
        List<Message> list = messageService.retrieveAllMessages();
        context.json(list).status(200);
    }

    /*
     * The handler retrieves a single message with a given ID. It supplies
     * the ID as an argument to retrieveMessageByMessageId from the service class
     * If successful, it returns the message with status:200
     * If no message by the given ID found, it returns empty string with status: 200
     */

    public void retrieveMessageByMessageId(Context context)throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Integer message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.retrieveMessageByMessageId(message_id);

        if(message != null){
            context.json(om.writeValueAsString(message)).status(200);
        }else {
            context.status(200).result("");
        }
    }

    /*
     * The handler invokes deleteMessageByMessageId from messageService and
     * deletes a message by the given ID if message exist with status:200
     * if the message doesn't exist, it returns an empty string with status:200
     */

    public void deleteMessageByMessageId(Context context) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Integer message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.deleteMessageByMessageId(message_id);

        if(message != null){
            context.json(om.writeValueAsString(message)).status(200);
        }else {
            context.status(200).result("");
        }
       
    }

    /*
     * Invokes updateMessageByMessageId from messageService by supplying message_id
     * as an argument. If message exists, it gets deleted with satus: 200
     * if not, it replys with staus:400 
     */

    public void updateMessageByMessageId(Context context) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message messageRequest = om.readValue(context.body(), Message.class);
        Integer message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.updateMessageByMessageId(message_id, messageRequest.getMessage_text());
        if(message != null) {
            context.json(om.writeValueAsString(message)).status(200);
        }else{
            context.status(400);
        }
    }

    /*
     * Retrievs all the messages by a given account_id
     * Replies with satus:200 messages by a user whether empty or not
     */

    public void retrieveAllMessagesForUser(Context context) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Integer account_id = Integer.valueOf(context.pathParam("account_id")); 
        List<Message> messages = messageService.retrieveAllMessagesForUser(account_id);

        context.json(om.writeValueAsString(messages)).status(200);
    }
}