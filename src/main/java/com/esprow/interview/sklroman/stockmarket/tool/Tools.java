package com.esprow.interview.sklroman.stockmarket.tool;

import java.util.UUID;

public class Tools {

    public static String generateOrderId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
