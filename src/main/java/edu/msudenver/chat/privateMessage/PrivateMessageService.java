package edu.msudenver.chat.privateMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<PrivateMessage> getPrivateMessages() {
        try {
            return privateMessageRepository.findAll();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO :Fix compile error
    public List<PrivateMessage> getPrivateMessage(Long playerId) {
        try {
            return privateMessageRepository.findByReceiverId(playerId);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PrivateMessage> getLatestPrivateMessages(Long playerId, LocalDateTime ldt) {
        try {
            return privateMessageRepository.findLatestPrivateMessages(playerId, ldt);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PrivateMessage> getLatestAreaMessages(Long playerId, LocalDateTime ldt) {
        try {
            return privateMessageRepository.findLatestAreaMessages(playerId, ldt);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public PrivateMessage savePrivateMessage(PrivateMessage privateMessage) {
        privateMessage = privateMessageRepository.saveAndFlush(privateMessage);
        entityManager.refresh(privateMessage);
        return privateMessage;
    }

}

