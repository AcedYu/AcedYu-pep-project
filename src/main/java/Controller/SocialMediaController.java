package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        // check if username is not blank
        if (account.getUsername() == "") {
            ctx.status(400);
            return;
        }
        // check if password follows guidelines
        if (account.getPassword().length() < 4) {
            ctx.status(400);
            return;
        }
        Account addedAccount = accountService.registerUser(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account login = accountService.login(account);
        if (login != null) {
            ctx.json(mapper.writeValueAsString(login));
            
        } else {
            ctx.status(401);
        }
    }
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Check if account exists
        Account existingAccount = accountService.getAccountById(message.getPosted_by());
        if (existingAccount == null) {
            ctx.status(400).json("");
            return;
        }

        // Check message length
        String message_text = message.getMessage_text();
        if (message_text == null || message_text.length() == 0 || message_text.length() > 254) {
            ctx.status(400).json("");
            return;
        }

        Message addedMessage = messageService.createMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
            
        } else {
            ctx.status(400);
        }
    }
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        
    }
    private void getMessageByIdHandler(Context ctx) {
        String id_string = ctx.pathParam("message_id");
        int id_int = Integer.parseInt(id_string);
        Message message = messageService.getMessageById(id_int);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
        
    }
    private void deleteMessageHandler(Context ctx) {
        String id_string = ctx.pathParam("message_id");
        int id_int = Integer.parseInt(id_string);
        Message deletedMessage = messageService.deleteMessage(id_int);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        String message_text = message.getMessage_text();
        String id_string = ctx.pathParam("message_id");
        int id_int = Integer.parseInt(id_string);

        if (message_text.length() == 0 || message_text.length() > 254) {
            ctx.status(400);
            return;
        }

        Message updatedMessage = messageService.updateMessage(id_int, message_text);
        System.out.println(updatedMessage);
        if (updatedMessage!= null) {
            ctx.json(mapper.writeValueAsString(updatedMessage));
            
        } else {
            ctx.status(400);
        }
    }
    private void getAccountMessagesHandler(Context ctx) {
        String id_string = ctx.pathParam("account_id");
        int id_int = Integer.parseInt(id_string);
        List<Message> messages = messageService.getUserMessages(id_int);
        ctx.json(messages);
        
    }

}