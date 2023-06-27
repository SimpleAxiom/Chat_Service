package edu.msudenver.chat.trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {


    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private EntityManager entityManager;

    private TradeService tradeService;

    @BeforeEach
    public void setup() {
        tradeService = new TradeService();
        tradeService.entityManager = entityManager;
        tradeService.tradeRepository = tradeRepository;
    }

    @Test
    public void testGetTrade() throws Exception {

        LocalDateTime dt = LocalDateTime.now();

        Trade trade = new Trade();
        trade.setTradeId(1L);
        trade.setTradeStatus((short)1);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(1L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)2);
        trade.setItem1Id(2L);
        trade.setPlayer1Id(1L);
        trade.setPlayer2Id(2L);

        Mockito.when(tradeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(trade));

        Trade actualTrade = tradeService.getTrade(1L);

        assertEquals(trade,actualTrade);
    }

    @Test
    public void testGetTradeNotFoundException() throws Exception {
        Mockito.when(tradeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertEquals(null, tradeService.getTrade(99L));
    }

    @Test
    public void testSaveTrade() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1L);
        trade.setTradeStatus((short)1);
        trade.setPlayer1Approval(true);
        trade.setItem2Quantity((short)1);
        trade.setItem2Id(1L);
        trade.setPlayer2Approval(true);
        trade.setItem1Quantity((short)2);
        trade.setItem1Id(2L);
        trade.setPlayer1Id(1L);
        trade.setPlayer2Id(2L);

        Mockito.when(tradeRepository.saveAndFlush(Mockito.any())).thenReturn(trade);
       // Mockito.when(tradeRepository.save(Mockito.any())).thenReturn(trade);

        assertEquals(trade, tradeService.saveTrade(trade));
    }

    @Test
    public void testSaveTradeBadRequest() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(3L);

        //Mockito.when(tradeRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(tradeRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.saveTrade(trade);
        });
    }
}
