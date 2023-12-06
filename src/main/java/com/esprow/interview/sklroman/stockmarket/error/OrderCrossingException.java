package com.esprow.interview.sklroman.stockmarket.error;

public class OrderCrossingException extends RuntimeException {
    public OrderCrossingException(String message) {
        super(message);
    }
}
