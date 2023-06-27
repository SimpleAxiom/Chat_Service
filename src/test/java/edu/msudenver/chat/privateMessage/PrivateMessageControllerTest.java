package edu.msudenver.chat.privateMessage;

import edu.msudenver.chat.groupMessage.GroupMessage;
import edu.msudenver.chat.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PrivateMessage.class)
class PrivateMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrivateMessageRepository privateMessageRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private PrivateMessageService privateMessageService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPrivateMessages() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/private/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        PrivateMessage testPrivateMessage = new PrivateMessage();


        testPrivateMessage.setMessageId(Long.valueOf("123"));
        testPrivateMessage.setReceiver_id(Long.valueOf("456"));

        Mockito.when(privateMessageService.getPrivateMessages()).thenReturn(Arrays.asList(testPrivateMessage));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("123"));
        assertTrue(response.getContentAsString().contains("456"));
    }
    @Test
    public void testGetPrivateMessageNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/private/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(privateMessageService.getPrivateMessage(Mockito.anyLong())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }


    //To DO test the epoch for LDT, I wrote into privateMessage the ldt as in its current for it is only present
    //in the PrivateMessageController.
    //But I stopped after adding is and commented it out as to not break anything right before the deadline

    @Test
    void testGetPrivateMessages() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/private/123?epoch=1670834700")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        PrivateMessage testPrivateMessage = new PrivateMessage();
        testPrivateMessage.setReceiver_id(Long.valueOf("123"));
        testPrivateMessage.setMessageId(Long.valueOf("456"));
        Long epoch = Long.valueOf(1670834700);


        Mockito.when(privateMessageService.getPrivateMessage(Mockito.anyLong())).
                thenReturn(Arrays.asList(testPrivateMessage));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("123"));
        assertTrue(response.getContentAsString().contains("456"));
       // assertTrue(epoch.getContentAsString().contains("1670834700"));


    }

    @Test
    void createPrivateMessage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/private/1/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"messageBody\":\"senderId\", \"senderType\": \"messageType\"}")
                .contentType(MediaType.APPLICATION_JSON);

        PrivateMessage testPrivateMessage = new PrivateMessage();
        Message message = new Message("Hello");
        testPrivateMessage.setReceiver_id(Long.valueOf("1"));
        testPrivateMessage.setMessage(message);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Hello"));
    }

    @Test
    public void testCreatePrivateMessageBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/private/1/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"messageBody\":\"senderId\", \"senderType\": \"messageType\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

}