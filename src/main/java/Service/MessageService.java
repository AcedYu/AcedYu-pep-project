package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.*;

public class MessageService {
    public MessageDAO messageDAO;
    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    public Message creatMessage(Message message) {
        return messageDAO.createMessage(message);
    }
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }
    public Message deleteMessage(int id) {
        Message deleteMessage = messageDAO.getMessageById(id);
        if (deleteMessage != null) {
            boolean success = messageDAO.deleteMessage(id);
            if (success) {
                return deleteMessage;
            }
        }
        return null;
    }
    public Message updateMessage(int id, String message) {
        messageDAO.updateMessage(id, message);
        return messageDAO.getMessageById(id);
    }
    public List<Message> getUserMessages(int userid) {
        return messageDAO.getUserMessages(userid);
    }
}
