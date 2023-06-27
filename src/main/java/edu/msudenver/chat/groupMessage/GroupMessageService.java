package edu.msudenver.chat.groupMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupMessageService {
    @Autowired
    protected GroupMessageRepository groupMessageRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<GroupMessage> getGroupMessages() {
        try {
            return groupMessageRepository.findAll();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GroupMessage> getGroupMessage(Long groupId) {
        try {
            return groupMessageRepository.findByGroupId(groupId);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GroupMessage> getLatestGroupMessages(Long groupId, LocalDateTime ldt) {
        try {
            return groupMessageRepository.findLatestGroupMessages(groupId, ldt);
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public GroupMessage saveGroupMessage(GroupMessage groupMessage) {
        groupMessage = groupMessageRepository.saveAndFlush(groupMessage);
        entityManager.refresh(groupMessage);
        return groupMessage;
    }
    
}
