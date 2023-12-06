package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.error.IllegalTradeTypeException;
import com.esprow.interview.sklroman.stockmarket.error.WrongSymbolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("order")
public class OrderController {

    private final Logger logger = LogManager.getLogger();

    private final TradesManager tradesManager;
    private final AccountValidator validator;

    public OrderController(TradesManager tradesManager, AccountValidator validator) {
        this.tradesManager = tradesManager;
        this.validator = validator;
    }

    @PostMapping("/{symbol}")
    public void place(@RequestHeader("clientId") String clientId, @PathVariable String symbol,
                      @Valid @RequestBody Order order) {
        if (!symbol.equals(order.getSymbol())) {
            throw new WrongSymbolException();
        }

        order.setClientId(clientId);
        switch (order.getTradeType()) {
            case BUY:
                validator.checkAccount(clientId);
                tradesManager.getOrderBook(symbol).placeBuyTradeOrder(order);
                break;
            case SELL:
                validator.checkStocks(clientId, symbol);
                tradesManager.getOrderBook(symbol).placeSellTradeOrder(order);
                break;
            default:
                throw new IllegalTradeTypeException();
        }
        logger.info(" New order placed: " + order);
    }

    @DeleteMapping("/{orderId}/{symbol}")
    public void cancel(@RequestHeader("orderId") String orderId, @PathVariable String symbol) {
        tradesManager.getOrderBook(symbol).cancelOrder(orderId);
    }

}
