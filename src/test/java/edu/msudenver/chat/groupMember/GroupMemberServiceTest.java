
package edu.msudenver.chat.groupMember;

import edu.msudenver.chat.groupMember.GroupMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.aspectj.runtime.internal.Conversions.longValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupMemberServiceTest {

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private EntityManager entityManager;

    private GroupMemberService groupMemberService;

    @BeforeEach
    public void setup() {
        groupMemberService = new GroupMemberService();
        groupMemberService.entityManager = entityManager;
        groupMemberService.groupMemberRepository = groupMemberRepository;
    }

    @Test
    public void testGetGroupMembers() throws Exception {
        GroupMember testGroupMember = new GroupMember();
        testGroupMember.setGroupId(1L);
        testGroupMember.setPlayerId(2L);

        Mockito.when(groupMemberRepository.findAll()).thenReturn(Arrays.asList(testGroupMember));

        List<GroupMember> groupMembers = groupMemberService.getGroupMembers();
        assertEquals(1, groupMembers.size());
        assertEquals(2L, groupMembers
                .get(0)
                .getPlayerId());
    }

    @Test
    public void testSaveGroupMember() throws Exception {
        GroupMember newGroupMember = new GroupMember();
        newGroupMember.setPlayerId(2L);
        newGroupMember.setGroupId(1L);

        Mockito.when(groupMemberRepository.saveAndFlush(Mockito.any())).thenReturn(newGroupMember);
        //Mockito.when(groupMemberRepository.save(Mockito.any())).thenReturn(newGroupMember);

        assertEquals(newGroupMember, groupMemberService.saveGroupMember(newGroupMember));
    }

    @Test
    public void testSaveGroupMemberBadRequest() throws Exception {
        GroupMember badNewGroupMember = new GroupMember();
        badNewGroupMember.setPlayerId(5L);

        //Mockito.when(groupMemberRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(groupMemberRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupMemberService.saveGroupMember(badNewGroupMember);
        });
    }

    @Test
    public void testDeleteGroupMember() throws Exception {
        GroupMember newGroupMember = new GroupMember();
        newGroupMember.setPlayerId(2L);
        newGroupMember.setGroupId(1L);
        //Mockito.when(groupMemberRepository.findById(Mockito.any())).thenReturn(Optional.of(newGroupMember));
        Mockito.when(groupMemberRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(groupMemberService.deleteGroupMember(1L, 2L));
    }

    @Test
    public void testDeleteGroupMemberNotFound() throws Exception {
        Mockito.when(groupMemberRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(groupMemberRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(groupMemberRepository)
                .deleteById(Mockito.any());

        assertFalse(groupMemberService.deleteGroupMember(2L, 1L));
    }
}

