package com.esprow.interview.sklroman.stockmarket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TradesExecutor {

    private final Logger logger = LogManager.getLogger();

    private final Map<String, MatchingEngine> engines;

    public TradesExecutor(Map<String, MatchingEngine> engines) {
        this.engines = engines;
    }

    @Async
    public void execute(OrderBook ob) {

        logger.debug(" Issuer: " + ob.getSymbol());

        ob.preparedForExecution();
        engines.get("Market").execute(ob);
        engines.get("Limit").execute(ob);

    }
}
