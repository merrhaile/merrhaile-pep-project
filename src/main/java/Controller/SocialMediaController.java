package Controller;

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
     * new user with an ID attached. If not, it returns null
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
     * It returns null if not
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

    private void retrieveAllMessages(Context context) {
        List<Message> list = messageService.retrieveAllMessages();
        context.json(list).status(200);
    }

    public void retrieveMessageByMessageId(Context context)throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Integer message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.retrieveMessageByMessageId(message_id);

        if(message != null){
            context.json(om.writeValueAsString(message)).status(200);
        }else {
            context.json(context.body()).status(200);
        }
    }
}