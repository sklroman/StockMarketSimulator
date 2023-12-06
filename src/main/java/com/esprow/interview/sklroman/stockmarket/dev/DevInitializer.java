package com.esprow.interview.sklroman.stockmarket.dev;

import com.esprow.interview.sklroman.stockmarket.Order;
import com.esprow.interview.sklroman.stockmarket.OrderController;
import com.esprow.interview.sklroman.stockmarket.data.*;
import com.esprow.interview.sklroman.stockmarket.enums.OrderType;
import com.esprow.interview.sklroman.stockmarket.enums.TradeType;
import com.esprow.interview.sklroman.stockmarket.tool.Tools;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Profile("dev")
@Service
public class DevInitializer {

    String symbol = "dev";

    private final OrderController orderController;
    private final ClientAccountRepository caRepo;
    private final ClientStocksRepository csRepo;


    public DevInitializer(OrderController orderController,
                          ClientAccountRepository caRepo, ClientStocksRepository csRepo) {
        this.orderController = orderController;
        this.caRepo = caRepo;
        this.csRepo = csRepo;
    }

    public void accounts() {

        System.out.println(" Accounts generator started... ");

        for (int i = 11; i < 20; i++) {
            String clientId = "client-" + i;
            ClientAccount clientAccount = new ClientAccount(clientId, 0L);
            ClientStocks clientStocks = new ClientStocks(new ClientStocksId(clientId, symbol), 100L);
            caRepo.save(clientAccount);
            csRepo.save(clientStocks);
        }
        for (int i = 21; i < 30; i++) {
            String clientId = "client-" + i;
            ClientAccount clientAccount = new ClientAccount(clientId, 5000L);
            ClientStocks clientStocks = new ClientStocks(new ClientStocksId(clientId, symbol), 0L);
            caRepo.save(clientAccount);
            csRepo.save(clientStocks);
        }

        System.out.println(" ...accounts generator complete ");
    }

    public void orders(int minCost, int maxCost, int iterations) {

        System.out.println(" Orders generator started... ");

        LinkedList<Order> genForBuy = new LinkedList<>();
        LinkedList<Order> genForSell = new LinkedList<>();

        for (int i = 0; i < iterations; i++) {
            genForSell.addAll(generateList(new Random().nextInt(maxCost - minCost + 1) + minCost, TradeType.SELL));
        }

        for (int i = 0; i < iterations; i++) {
            genForBuy.addAll(generateList(new Random().nextInt(maxCost - minCost + 1) + minCost, TradeType.BUY));
        }

//        genForSell.addAll(generateList(111, TradeType.SELL));
//        genForSell.addAll(generateList(109, TradeType.SELL));
//        genForSell.addAll(generateList(108, TradeType.SELL));
//        genForSell.addAll(generateList(106, TradeType.SELL));
//        genForSell.addAll(generateList(104, TradeType.SELL));
//        genForSell.addAll(generateList(103, TradeType.SELL));
//
//        genForBuy.addAll(generateList(108, TradeType.BUY));
//        genForBuy.addAll(generateList(106, TradeType.BUY));
//        genForBuy.addAll(generateList(105, TradeType.BUY));
//        genForBuy.addAll(generateList(102, TradeType.));
//        genForBuy.addAll(generateList(101, TradeType.BUY));
//        genForBuy.addAll(generateList(100, TradeType.BUY));

        for (Order order : genForSell) {
            String clientId = "client-" + (new Random().nextInt(9) + 11);
            orderController.place(clientId, symbol, order);

        }

        for (Order order : genForBuy) {
            String clientId = "client-" + (new Random().nextInt(9) + 21);
            orderController.place(clientId, symbol, order);
        }

        System.out.println(" ...orders generator complete ");

    }

    private LinkedList<Order> generateList(Integer cost, TradeType type) {
        LinkedList<Order> list = new LinkedList<>();
        int ii = new Random().nextInt(5) + 2;
        for (int i = 0; i < ii; i++) {
            Order order = new Order();
            order.setSymbol("dev");
            order.setCost(cost);
            order.setTradeType(type);
            order.setOrderType(OrderType.LIMIT);
            Integer qty = new Random().nextInt(9) + 1;
            order.setQty(qty);
            list.add(order);
            Tools.delay(100);
        }
        return list;
    }


}
