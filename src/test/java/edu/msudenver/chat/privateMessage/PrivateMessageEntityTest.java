package edu.msudenver.chat.privateMessage;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PrivateMessage.class)
public class PrivateMessageEntityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrivateMessageRepository privateMessageRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

}
