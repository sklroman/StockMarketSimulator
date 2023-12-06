package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.dev.DevInitializer;
import com.esprow.interview.sklroman.stockmarket.tool.Flags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class StockMarketSimulatorApplication {

    @Autowired
    private Environment environment;
    @Autowired(required = false)
    private DevInitializer devInitializer;

    public static void main(String[] args) {
        SpringApplication.run(StockMarketSimulatorApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() {
        for (String profile : environment.getActiveProfiles()) {
            if ("dev".equals(profile)) {
                Flags.setDevTrue();
                devInitializer.accounts();
                devInitializer.orders(100, 150, 10);
            }
        }
    }
}
