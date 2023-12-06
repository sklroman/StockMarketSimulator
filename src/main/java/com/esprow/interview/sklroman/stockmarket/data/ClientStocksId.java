package com.esprow.interview.sklroman.stockmarket.data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ClientStocksId implements Serializable {

    private String clientId;
    private String symbol;

    public ClientStocksId(String clientId, String symbol) {
        this.clientId = clientId;
        this.symbol = symbol;
    }

    public ClientStocksId() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "ClientStocksId{" +
                "clientId='" + clientId + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientStocksId that = (ClientStocksId) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, symbol);
    }
}
