package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
   
    public Message addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public Message getMessageByID(int messageId) {
        return messageDAO.getMessageByID(messageId);
    }

    public Message deleteMessageByID(int messageId) {
        return messageDAO.deleteMessageByID(messageId);
    }

    public Message updateMessageByID(int messageID, String message_text) {
        return messageDAO.updateMessageByID(messageID, message_text);
    }

    public List<Message> getAllMessagesByAccount(int accountId) {
        return messageDAO.getAllMessagesByAccount(accountId);
    }
}
