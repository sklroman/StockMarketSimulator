package com.esprow.interview.sklroman.stockmarket.scheduler;

import com.esprow.interview.sklroman.stockmarket.TradesManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TradesJob {

    private final TradesManager tradesManager;

    public TradesJob(TradesManager tradesManager) {
        this.tradesManager = tradesManager;
    }

    @Scheduled(fixedRateString = "${tact.period}")
    public void scheduleTact() throws InterruptedException {
        tradesManager.tact();
    }

}
