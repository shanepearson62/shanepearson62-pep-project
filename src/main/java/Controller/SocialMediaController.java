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
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // post register
        app.post("/register", this::registerHandler);

        // post login
        app.post("/login", this::loginHandler);

        // post message
        app.post("/messages", this::postMessageHandler);

        // get all messages
        app.get("/messages", this::getAllMessagesHandler);

        // get message by id
        app.get("/messages/{message_id}", this::getMessageByID);

        // delete message by id
        app.delete("/messages/{message_id}", this::deleteMessageByID);

        // patch message by id
        app.patch("/messages/{message_id}", this::updateMessageByID);

        // get all messages by account
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccount);

        return app;
    }

    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        System.out.print("user: " +account.username + " pass: "+account.password);
        if (account.getUsername().isBlank() || account.getPassword().length() < 4 || accountService.existsByUsername(account.getUsername())) {
            ctx.status(400);
        } else {
            Account createdAccount = accountService.createAccount(account.getUsername(), account.getPassword());
            ctx.json(createdAccount);
            ctx.status(200);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account existingAccount = accountService.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existingAccount != null) {
            ctx.json(existingAccount);
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if(message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 || !accountService.accountExistsByID(message.posted_by)) {
            ctx.status(400);
        } else {
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage != null) {
                ctx.json(addedMessage);
                ctx.status(200);
            } 
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    private void getMessageByID(Context ctx) {
        String holder = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(holder);
        Message message = messageService.getMessageByID(messageId);
        if(message != null) {
            ctx.json(message);
        }
        ctx.status(200);
    }

    private void deleteMessageByID(Context ctx) {
        String holder = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(holder);
        Message message = messageService.deleteMessageByID(messageId);
        if (message != null) {
            ctx.json(message);
        }
        ctx.status(200);
    }

    private void updateMessageByID(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        message.setMessage_id(Integer.parseInt(ctx.pathParam("message_id")));

        if(message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            ctx.status(400);
        } else {
            Message updatedMessage = messageService.updateMessageByID(message.getMessage_id(), message.getMessage_text());
            if (updatedMessage != null) {
                ctx.json(updatedMessage);
                ctx.status(200);
            }
            else {
                ctx.status(400);
            }
        }
    }

    private void getAllMessagesByAccount(Context ctx) {
        String holder = ctx.pathParam("account_id");
        int accountId = Integer.parseInt(holder);
        List<Message> messages = messageService.getAllMessagesByAccount(accountId);
        ctx.json(messages);
        ctx.status(200);
    }
}