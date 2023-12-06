package com.esprow.interview.sklroman.stockmarket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TradesManager {

    private final Logger logger = LogManager.getLogger();

    private final TradesExecutor executor;

    private final Map<String, OrderBook> books = new ConcurrentHashMap<>();

    private Integer executionCounter = 0;

    public TradesManager(TradesExecutor executor) {
        this.executor = executor;
    }

    public OrderBook getOrderBook(String symbol) {
        if (books.containsKey(symbol)) {
            return books.get(symbol);
        }
        OrderBook ob = new OrderBook(symbol);
        books.put(symbol, ob);
        return ob;
    }

    public void tact() {
        executionCounter++;
        long t1 = System.currentTimeMillis();
        logger.debug(" Tact: " + executionCounter + ", time: " +
                DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));

        for (OrderBook ob : books.values()) {
            if (!ob.isExecutingNow()) {
                ob.setExecutingNow(true);
                executor.execute(ob);
                ob.setExecutingNow(false);
            }
        }

        long t2 = System.currentTimeMillis() - t1;
        logger.debug(", execution time: " + t2);
    }

}
