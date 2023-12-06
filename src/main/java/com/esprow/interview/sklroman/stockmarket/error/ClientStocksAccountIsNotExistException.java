package com.esprow.interview.sklroman.stockmarket.error;

public class ClientStocksAccountIsNotExistException extends RuntimeException {
    public ClientStocksAccountIsNotExistException(String clientId) {
        super("Client, ID:" + clientId + " hasn't stock account");
    }
}
