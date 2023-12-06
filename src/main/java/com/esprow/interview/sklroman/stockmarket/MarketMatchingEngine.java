package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.data.ClientAccountRepository;
import com.esprow.interview.sklroman.stockmarket.data.ClientStocksRepository;
import com.esprow.interview.sklroman.stockmarket.data.CompletedTradesRepository;
import org.springframework.stereotype.Service;

@Service("Market")
public class MarketMatchingEngine implements MatchingEngine {

    private final ClientAccountRepository caRepo;
    private final ClientStocksRepository csRepo;
    private final CompletedTradesRepository ctRepo;

    public MarketMatchingEngine(
            ClientAccountRepository caRepo,
            ClientStocksRepository csRepo,
            CompletedTradesRepository ctRepo) {
        this.caRepo = caRepo;
        this.csRepo = csRepo;
        this.ctRepo = ctRepo;
    }

    public void execute(OrderBook ob) {
        //market cost orders processing logic
    }

}
