package edu.msudenver.chat.groupMember;

import edu.msudenver.chat.message.MessageService;
import edu.msudenver.chat.privateMessage.PrivateMessageService;
import edu.msudenver.chat.trade.TradeService;
import edu.msudenver.externalclient.playerclient;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GroupMemberController.class)
public class GroupMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupMemberService groupMemberService;

    @MockBean
    private playerclient playerClient;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetGroupMembers() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/groups/1/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        GroupMember member = new GroupMember();
        member.setGroupId(1L);
        member.setPlayerId(13L);

        Mockito.when(groupMemberService.getGroupMembers()).thenReturn(Arrays.asList(member));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("13"));
    }


    @Test
    public void testCreateGroupMember() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/1/members")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": \"13\"}")
                .contentType(MediaType.APPLICATION_JSON);

        GroupMember member = new GroupMember();
        member.setGroupId(1L);
        member.setPlayerId(13L);
        Mockito.when(groupMemberService.saveGroupMember(Mockito.any())).thenReturn(member);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("13"));
    }

    @Test
    public void testCreateGroupMemberBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/chat/groups/1/members/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"playerId\": \"13\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupMemberService.saveGroupMember(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteGroupMember() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/chat/groups/1/members/13")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupMemberService.deleteGroupMember(Mockito.any(), Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteGroupMemberNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/chat/groups/1/members/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(groupMemberService.deleteGroupMember(Mockito.any(), Mockito.any())).thenReturn(false);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
