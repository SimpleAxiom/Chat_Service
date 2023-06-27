package edu.msudenver.chat.groupMessage;

import edu.msudenver.chat.groupMessage.GroupMessageService;
import edu.msudenver.chat.groupMessage.GroupMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupMessageServiceTest {

    @MockBean
    private GroupMessageRepository groupMessageRepository;

    @MockBean
    private EntityManager entityManager;

    private GroupMessageService groupMessageService;
    
    @BeforeEach
    public void setup() {
        groupMessageService = new GroupMessageService();
        groupMessageService.entityManager = entityManager;
        groupMessageService.groupMessageRepository = groupMessageRepository;
    }

    @Test
    public void testGetGroupMessages() throws Exception {
        GroupMessage testGroupMessage = new GroupMessage();
        testGroupMessage.setGroupId(1L);
        testGroupMessage.setMessageId(21L);

        Mockito.when(groupMessageRepository.findAll()).thenReturn(Arrays.asList(testGroupMessage));

        List<GroupMessage> groupMessages = groupMessageService.getGroupMessages();
        assertEquals(1, groupMessages.size());
        assertEquals(1, groupMessages
                .get(0)
                .getMessageId());
    }

    @Test
    public void testSaveGroupMessage() throws Exception {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setMessageId(25L);
        groupMessage.setGroupId(1L);

        Mockito.when(groupMessageRepository.saveAndFlush(Mockito.any())).thenReturn(groupMessage);
        Mockito.when(groupMessageRepository.save(Mockito.any())).thenReturn(groupMessage);

        assertEquals(groupMessage, groupMessageService.saveGroupMessage(groupMessage));
    }

    @Test
    public void testSaveGroupMessageBadRequest() throws Exception {
        GroupMessage badGroupMessage = new GroupMessage();
        badGroupMessage.setMessageId(25L);

        Mockito.when(groupMessageRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(groupMessageRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            groupMessageService.saveGroupMessage(badGroupMessage);
        });
    }
}