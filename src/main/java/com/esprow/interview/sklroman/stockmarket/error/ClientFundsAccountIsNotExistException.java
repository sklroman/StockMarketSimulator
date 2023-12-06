package com.esprow.interview.sklroman.stockmarket.error;

public class ClientFundsAccountIsNotExistException extends RuntimeException {
    public ClientFundsAccountIsNotExistException(String clientId) {
        super("Client, ID:" + clientId + " hasn't funds account");
    }
}
