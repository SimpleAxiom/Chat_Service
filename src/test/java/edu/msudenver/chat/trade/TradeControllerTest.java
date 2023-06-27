package edu.msudenver.chat.trade;

import edu.msudenver.chat.message.Message;
import edu.msudenver.chat.message.MessageService;
import edu.msudenver.chat.privateMessage.PrivateMessage;
import edu.msudenver.chat.privateMessage.PrivateMessageService;
import edu.msudenver.externalclient.playerclient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private PrivateMessageService privateMessageService;

    @MockBean
    private playerclient playerClient;

    //Not quite sure why this was needed, but taking it out threw errors requesting it to be added
    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @MockBean
    private TradeService tradeService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetTrades() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/trades/")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)2222);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)3333);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)5555);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Mockito.when(tradeService.getTrades()).thenReturn(Arrays.asList(trade));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("1111"));
        assertTrue(response.getContentAsString().contains("2222"));
        assertTrue(response.getContentAsString().contains("3333"));
        assertTrue(response.getContentAsString().contains("4444"));
        assertTrue(response.getContentAsString().contains("5555"));
        assertTrue(response.getContentAsString().contains("6666"));
        assertTrue(response.getContentAsString().contains("7777"));
        assertTrue(response.getContentAsString().contains("8888"));
    }

    @Test
    public void testGetTrade() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/trades/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)2222);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)3333);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)5555);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Mockito.when(tradeService.getTrade(Mockito.anyLong())).thenReturn(trade);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("1111"));
        assertTrue(response.getContentAsString().contains("2222"));
        assertTrue(response.getContentAsString().contains("3333"));
        assertTrue(response.getContentAsString().contains("4444"));
        assertTrue(response.getContentAsString().contains("5555"));
        assertTrue(response.getContentAsString().contains("6666"));
        assertTrue(response.getContentAsString().contains("7777"));
        assertTrue(response.getContentAsString().contains("8888"));
    }

    @Test
    public void testGetTradeNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/trades/99")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(tradeService.getTrade(Mockito.anyLong())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateTrade() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/trades")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"item1Quantity\": 1, \"item1Id\": 6666, \"player1Id\": 7777, \"player2Id\": 8888}")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)2222);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);

        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    @Test
    public void testCreateTradeBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/trades")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"tradeId\":1111, \"tradeStatus\": 2222}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(tradeService.saveTrade(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateTrade() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/9")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"item2Quantity\": 1, \"item2Id\": 3333}")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)2222);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void testUpdateTradeBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"tradeId\":1111, \"tradeStatus\": 2222}")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)2222);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateTradeNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"tradeId\":1111, \"tradeStatus\": 2222}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());

    }

    @Test
    public void testApproveTrade() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/9/1?approval=true")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(9L);
        trade.setTradeStatus((short)0);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(1L);
        trade.setPlayer2Id(2L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void testApproveTradeBadRequestMissingApproval() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/9/1")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)0);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void testApproveTradeBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/1/1?approval=true")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1111L);
        trade.setTradeStatus((short)0);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);


        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);


        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void testApproveTradeNotAcceptable() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/1/1?approval=true")
                .contentType(MediaType.APPLICATION_JSON);

        Trade trade = new Trade();
        trade.setTradeId(1L);
        trade.setTradeStatus((short)2);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(4444L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)1);
        trade.setItem1Id(6666L);
        trade.setPlayer1Id(7777L);
        trade.setPlayer2Id(8888L);

        Message message = new Message();
        message.setMessageBody("Hi");
        message.setDateTime(LocalDateTime.now());
        message.setSenderId(1L);
        message.setSenderType("PLAYER");
        message.setMessageType("TRADE");

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageId(1L);
        privateMessage.setReceiver_id(2L);


        Mockito.when(messageService.saveMessage(Mockito.any())).thenReturn(message);
        Mockito.when(privateMessageService.savePrivateMessage(Mockito.any())).thenReturn(privateMessage);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(trade);
        Mockito.when(tradeService.saveTrade(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());
    }

    @Test
    public void testApprovalTradeNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/trades/1/1?approval=true")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(tradeService.getTrade(Mockito.any())).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
/*
    @Test
    public void testGetGroup() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        LocalDateTime dt = LocalDateTime.now();
        Group testGroup = new Group();
        testGroup.setGroupID(1L);
        testGroup.setGroupName("Test Group");
        testGroup.setGroupCreationDate(dt);

        Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testGroup));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Test Group"));
    }

    @Test
    public void testGetGroupNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/groups/7")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateGroup() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupName\":\"Test Group\", \"groupCreationDate\": \"2022-02-15T00:00:00\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Group group = new Group();
        group.setGroupName("Test Group");

        Mockito.when(groupRepository.saveAndFlush(Mockito.any())).thenReturn(group);
        Mockito.when(groupRepository.save(Mockito.any())).thenReturn(group);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Test Group"));
    }

    @Test
    public void testCreateGroupBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupName\": \"Test Group\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(groupRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateGroup() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupName\":\"Tester Group\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Group group = new Group();
        group.setGroupName("Tester Group");

        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        Group groupUpdated = new Group();
        groupUpdated.setGroupName("Group Updated");

        Mockito.when(groupRepository.save(Mockito.any())).thenReturn(groupUpdated);
        Mockito.when(groupRepository.saveAndFlush(Mockito.any())).thenReturn(groupUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Group Updated"));
    }

    @Test
    public void testUpdateGroupNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/groups/7")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupId\":\"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateGroupBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/chat/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"groupName\":\"No Group\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Group group = new Group();
        group.setGroupName("Bleh");

        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        Mockito.when(groupRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(groupRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/chat/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Group group =  new Group();
        group.setGroupName("New Group");

        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));
        Mockito.when(groupRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteGroupNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/chat/groups/7")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(groupRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(groupRepository)
                .deleteById(Mockito.any());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    */
}


/*

​
    @Test
    public void testUpdateCountry() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/countries/ca")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"ca\", \"countryName\": \"Canada Updated\"}")
                .contentType(MediaType.APPLICATION_JSON);
​
        Country canada = new Country();
        canada.setCountryName("Canada");
        canada.setCountryCode("ca");
        Mockito.when(countryService.getCountry(Mockito.any())).thenReturn(canada);
​
        Country canadaUpdated = new Country();
        canadaUpdated.setCountryName("Canada Updated");
        canadaUpdated.setCountryCode("ca");
        Mockito.when(countryService.saveCountry(Mockito.any())).thenReturn(canadaUpdated);
​
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
​
        MockHttpServletResponse response = result.getResponse();
​
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Canada Updated"));
    }
​
    @Test
    public void testUpdateCountryNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/countries/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);
​
        Mockito.when(countryService.getCountry(Mockito.any())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
​
        MockHttpServletResponse response = result.getResponse();
​
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
​
    @Test
    public void testUpdateCountryBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/countries/ca")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"ca\"}")
                .contentType(MediaType.APPLICATION_JSON);
​
        Country canada = new Country();
        canada.setCountryName("Canada");
        canada.setCountryCode("ca");
        Mockito.when(countryService.getCountry(Mockito.any())).thenReturn(canada);
​
        Mockito.when(countryService.saveCountry(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
​
        MockHttpServletResponse response = result.getResponse();
​
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }
​
    @Test
    public void testDeleteCountry() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/countries/ca")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
​
        Mockito.when(countryService.deleteCountry(Mockito.any())).thenReturn(true);
​
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
​
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }
​
    @Test
    public void testDeleteCountryNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/countries/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
​
        Mockito.when(countryService.deleteCountry(Mockito.any())).thenReturn(false);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
​
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
 */