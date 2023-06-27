package edu.msudenver.chat.groupMember;

import edu.msudenver.chat.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupMemberService {
    @Autowired
    protected GroupMemberRepository groupMemberRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<GroupMember> getGroupMembers() {
        return groupMemberRepository.findAll();
    }

    public GroupMember getGroupMember(Long groupId, Long playerId) {
        GroupMemberId groupMemberId = new GroupMemberId(groupId, playerId);
        try {
            return groupMemberRepository.findById(groupMemberId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public GroupMember saveGroupMember(GroupMember groupMember) {
        groupMember = groupMemberRepository.saveAndFlush(groupMember);
        entityManager.refresh(groupMember);
        return groupMember;
    }

    public boolean deleteGroupMember(Long groupId, Long playerId) {
        GroupMemberId groupMemberId = new GroupMemberId(groupId, playerId);
        try {
            if(groupMemberRepository.existsById(groupMemberId)) {
                groupMemberRepository.deleteById(groupMemberId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }
}
