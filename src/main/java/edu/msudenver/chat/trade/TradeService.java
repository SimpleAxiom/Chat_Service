package edu.msudenver.chat.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TradeService {
    @Autowired
    protected TradeRepository tradeRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Trade> getTrades() {
        return tradeRepository.findAll();
    }

    public Trade getTrade(Long tradeId) {
        try {
            return tradeRepository.findById(tradeId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Trade saveTrade(Trade trade) {
        trade = tradeRepository.saveAndFlush(trade);
        entityManager.refresh(trade);
        return trade;
    }

    public boolean deleteTrade(Long tradeId) {
        try {
            if(tradeRepository.existsById(tradeId)) {
                tradeRepository.deleteById(tradeId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }
}
