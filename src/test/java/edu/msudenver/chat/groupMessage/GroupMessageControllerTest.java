package edu.msudenver.chat.groupMessage;

import edu.msudenver.chat.message.Message;
import edu.msudenver.chat.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GroupMessageController.class)
public class GroupMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @MockBean
    private GroupMessageService groupMessageService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetGroupMessage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/groups/1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        GroupMessage testGroupMessage = new GroupMessage();
        Message message = new Message("Hello");
        testGroupMessage.setMessageId(25L);
        testGroupMessage.setMessage(message);

        Mockito.when(groupMessageService.getGroupMessage(25L)).thenReturn(Arrays.asList(testGroupMessage));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Hello"));
    }



    @Test
    public void testCreateGroupMessage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/1/messages")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupMessageId\":\"messageId\", \"groupMessage\": \"message\"}")
                .contentType(MediaType.APPLICATION_JSON);

        GroupMessage testGroupMessage = new GroupMessage();
        Message message = new Message("Hey");
        testGroupMessage.setMessage(message);
        testGroupMessage.setMessageId(25L);
        Mockito.when(groupMessageService.saveGroupMessage(Mockito.any())).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Hey"));
    }

    @Test
    public void testCreateGroupMessageBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/1/messages/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupMessageId\":\"messageId\", \"groupMessage\": \"message\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupMessageService.saveGroupMessage(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }
}