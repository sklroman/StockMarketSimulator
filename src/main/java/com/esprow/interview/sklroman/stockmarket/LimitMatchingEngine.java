package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.data.*;
import com.esprow.interview.sklroman.stockmarket.enums.OrderStatus;
import com.esprow.interview.sklroman.stockmarket.ws.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("Limit")
public class LimitMatchingEngine implements MatchingEngine {

    private final Logger logger = LogManager.getLogger();

    private final WebSocketService wss;
    private final ClientAccountRepository caRepo;
    private final ClientStocksRepository csRepo;
    private final CompletedTradesRepository ctRepo;

    public LimitMatchingEngine(
            WebSocketService wss, ClientAccountRepository caRepo,
            ClientStocksRepository csRepo, CompletedTradesRepository ctRepo) {
        this.wss = wss;
        this.caRepo = caRepo;
        this.csRepo = csRepo;
        this.ctRepo = ctRepo;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void execute(OrderBook ob) {
        if (!ob.getForSell().isEmpty() && !ob.getForBuy().isEmpty()) {

            ob.log();

            Integer dealCost = ob.getForSell().firstKey();

            Integer minBuyCost;
            do {
                ob.cleaning();
                minBuyCost = ob.getForBuy().ceilingKey(dealCost);

                if (minBuyCost != null) {

                    Map<Integer, LinkedList<Order>> buyMap = ob.getForBuy().tailMap(minBuyCost, true);
                    SortedSet<Order> sortedByTimeBuys = new TreeSet<>(Comparator.comparing(Order::getCreated));
                    buyMap.keySet().stream().map(buyMap::get).forEach(sortedByTimeBuys::addAll);
                    LinkedList<Order> buyOrders = new LinkedList<>(sortedByTimeBuys);

                    LinkedList<Order> sellOrders = ob.getForSell().get(dealCost);

                    matching(sellOrders, buyOrders, ob);
                }

                dealCost = ob.getForSell().higherKey(dealCost);
            } while (minBuyCost != null && dealCost != null);
        }
    }


    private void matching(LinkedList<Order> sellOrders, LinkedList<Order> buyOrders, OrderBook ob) {

        while (!sellOrders.isEmpty() && !buyOrders.isEmpty()) {

            Order sellOrder = sellOrders.getFirst();
            Order buyOrder = buyOrders.getFirst();

            Integer dealCost = sellOrder.getCost();
            ob.setLast(dealCost);

            Optional<ClientAccount> sellerAccount = caRepo.findById(sellOrder.getClientId());
            Optional<ClientAccount> buyerAccount = caRepo.findById(buyOrder.getClientId());
            Optional<ClientStocks> sellerStocks = csRepo.findById(new ClientStocksId(sellOrder.getClientId(), sellOrder.getSymbol()));
            Optional<ClientStocks> buyerStocks = csRepo.findById(new ClientStocksId(buyOrder.getClientId(), buyOrder.getSymbol()));
            if (sellerAccount.isEmpty()) {
                sellerAccount = Optional.of(new ClientAccount(sellOrder.getClientId(), 0L));
            }
            if (buyerStocks.isEmpty()) {
                buyerStocks = Optional.of(new ClientStocks(new ClientStocksId(buyOrder.getClientId(), buyOrder.getSymbol()), 0L));
            }

            Integer dealQty = 0;
            Integer dealSum = 0;

            if (sellOrder.getStock() > buyOrder.getStock()) {
                dealQty = buyOrder.getStock();
                dealSum = dealQty * dealCost;
                sellOrder.setStatus(OrderStatus.PARTIALLY_COMPLETED);
                buyOrder.setStatus(OrderStatus.COMPLETED);
                buyOrders.remove(buyOrder);
            }
            if (sellOrder.getStock() < buyOrder.getStock()) {
                dealQty = sellOrder.getStock();
                dealSum = dealQty * dealCost;
                buyOrder.setStatus(OrderStatus.PARTIALLY_COMPLETED);
                sellOrder.setStatus(OrderStatus.COMPLETED);
                sellOrders.remove(sellOrder);
            }
            if (sellOrder.getStock().equals(buyOrder.getStock())) {
                dealQty = sellOrder.getStock();
                dealSum = dealQty * dealCost;
                sellOrder.setStatus(OrderStatus.COMPLETED);
                buyOrder.setStatus(OrderStatus.COMPLETED);
                sellOrders.remove(sellOrder);
                buyOrders.remove(buyOrder);
            }

            sellOrder.reduceStock(dealQty);
            buyOrder.reduceStock(dealQty);

            //проверка баланса покупателя
            if (buyerAccount.get().getAmount() < dealSum) {
                buyOrder.setStatus(OrderStatus.INSUFFICIENT_FUNDS);
                buyOrders.remove(buyOrder);
                logger.debug(" The client does not have enough FUNDS, client ID: " + buyOrder.getClientId());
                return;
            }
            //проверка наличия объема предложения
            if (sellerStocks.get().getQty() < dealQty) {
                sellOrder.setStatus(OrderStatus.INSUFFICIENT_STOCKS);
                sellOrders.remove(sellOrder);
                logger.debug(" The client does not have enough STOCKS, client ID: " + buyOrder.getClientId());
                return;
            }

            reportOfTrades(sellOrder, buyOrder, dealQty, dealSum);

            sellerAccount.get().increaseAmount(dealSum);
            sellerStocks.get().reduceQty(dealQty);

            buyerAccount.get().reduceAmount(dealSum);
            buyerStocks.get().increaseQty(dealQty);

            caRepo.saveAndFlush(sellerAccount.get());
            caRepo.saveAndFlush(buyerAccount.get());
            csRepo.saveAndFlush(sellerStocks.get());
            csRepo.saveAndFlush(buyerStocks.get());
        }
    }

    private void reportOfTrades(Order sellOrder, Order buyOrder, Integer dealQty, Integer dealSum) {

        if (dealQty == 0) {
            throw new RuntimeException(" reportOfTrades dealQty IS NULL");
        }

        CompletedTrades completedSell = new CompletedTrades(sellOrder)
                .setCost(sellOrder.getCost())
                .setQty(dealQty)
                .setSum(dealSum)
                .setCounterClientId(buyOrder.getClientId());
        ctRepo.save(completedSell);
        wss.sendMsg(sellOrder.getClientId(), completedSell.toString());
        CompletedTrades completedBuy = new CompletedTrades(buyOrder)
                .setCost(sellOrder.getCost())
                .setQty(dealQty)
                .setSum(dealSum)
                .setCounterClientId(sellOrder.getClientId());
        ctRepo.save(completedBuy);
        wss.sendMsg(buyOrder.getClientId(), completedBuy.toString());
    }

}
