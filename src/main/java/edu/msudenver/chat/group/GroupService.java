package edu.msudenver.chat.group;

import edu.msudenver.chat.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupService {
    @Autowired
    protected GroupRepository groupRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group getGroup(Long groupId) {
        try {
            return groupRepository.findById(groupId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Group saveGroup(Group group) {
        group = groupRepository.saveAndFlush(group);
        entityManager.refresh(group);
        return group;
    }

    public boolean deleteGroup(Long groupId) {
        try {
            if(groupRepository.existsById(groupId)) {
                groupRepository.deleteById(groupId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }
}
