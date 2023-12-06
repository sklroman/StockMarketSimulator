package com.esprow.interview.sklroman.stockmarket.error;

public class WrongSymbolException extends RuntimeException {
    public WrongSymbolException() {
        super("The symbol in the body and the path must be the same");
    }
}
