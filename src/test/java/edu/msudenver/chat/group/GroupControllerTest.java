package edu.msudenver.chat.group;

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
@WebMvcTest(value = GroupController.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @SpyBean
    private GroupService groupService;

    @BeforeEach
    public void setup() {
        groupService.entityManager = entityManager;
    }

    @Test
    public void testGetGroups() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/chat/groups/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        LocalDateTime dt = LocalDateTime.now();
        Group testGroup = new Group();
        testGroup.setGroupID(1L);
        testGroup.setGroupName("Test Group");
        testGroup.setGroupCreationDate(dt);

        Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList(testGroup));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Test Group"));
    }

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
}
