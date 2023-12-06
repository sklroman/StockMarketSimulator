package com.esprow.interview.sklroman.stockmarket.error;

public class IllegalTradeTypeException extends RuntimeException {
    public IllegalTradeTypeException() {
        super("Field tradeType contains illegal value");
    }
}
