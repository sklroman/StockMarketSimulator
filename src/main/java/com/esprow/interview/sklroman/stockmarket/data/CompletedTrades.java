package com.esprow.interview.sklroman.stockmarket.data;

import com.esprow.interview.sklroman.stockmarket.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CompletedTrades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tradeType;
    private String clientId;
    private String symbol;
    private Integer cost;
    private Integer qty;
    private Integer sum;
    private String counterClientId;
    private Long created;

    public CompletedTrades() {
    }

    public CompletedTrades(Order order) {
        this.tradeType = order.getTradeType().toString();
        this.clientId = order.getClientId();
        this.symbol = order.getSymbol();
        created = System.currentTimeMillis();
    }

    public String getCounterClientId() {
        return counterClientId;
    }

    public CompletedTrades setCounterClientId(String counterClientId) {
        this.counterClientId = counterClientId;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getClientId() {
        return clientId;
    }

    public Long getId() {
        return id;
    }

    public CompletedTrades setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public CompletedTrades setCost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getQty() {
        return qty;
    }

    public CompletedTrades setQty(Integer qty) {
        this.qty = qty;
        return this;
    }

    public Integer getSum() {
        return sum;
    }

    public CompletedTrades setSum(Integer sum) {
        this.sum = sum;
        return this;
    }

    public Long getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "CompletedTrades{" +
                "id=" + id +
                ", tradeType='" + tradeType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", cost=" + cost +
                ", qty=" + qty +
                ", sum=" + sum +
                ", counterClientId='" + counterClientId + '\'' +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedTrades that = (CompletedTrades) o;
        return Objects.equals(id, that.id) && Objects.equals(tradeType, that.tradeType) && Objects.equals(clientId, that.clientId) && Objects.equals(symbol, that.symbol) && Objects.equals(cost, that.cost) && Objects.equals(qty, that.qty) && Objects.equals(sum, that.sum) && Objects.equals(counterClientId, that.counterClientId) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tradeType, clientId, symbol, cost, qty, sum, counterClientId, created);
    }
}
