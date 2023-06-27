package edu.msudenver.chat.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Message> getPrivateMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessages(String messageType) {
        try {
            return messageRepository.findByMessageType(messageType);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getPlayerMessages(Long playerId, String messageType) {
        try {
            return messageRepository.findByMessageType(messageType);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message getMessage(Long messageId) {
        try {
            return messageRepository.findById(messageId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Message saveMessage(Message message) {
        message = messageRepository.saveAndFlush(message);
        entityManager.refresh(message);
        return message;
    }
}

