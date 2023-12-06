package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.data.*;
import com.esprow.interview.sklroman.stockmarket.enums.TradeType;
import com.esprow.interview.sklroman.stockmarket.tool.Tools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeneralProcessingTest_Ok {

    @Autowired
    private WebApplicationContext web;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientAccountRepository caRepo;
    @Autowired
    private ClientStocksRepository csRepo;
    @Autowired
    private CompletedTradesRepository ctRepo;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.web).build();

        //sellers
        caRepo.save(new ClientAccount("client-11", 0L));
        csRepo.save(new ClientStocks(new ClientStocksId("client-11", "dev"), 100L));

        csRepo.save(new ClientStocks(new ClientStocksId("client-12", "dev"), 100L));

        //buyers
        caRepo.save(new ClientAccount("client-21", 5000L));
        csRepo.save(new ClientStocks(new ClientStocksId("client-21", "dev"), 0L));

        caRepo.save(new ClientAccount("client-22", 5000L));
    }

    @Test
    void startOk() throws Exception {
        String[] sellBodies1 = {
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":109,\"qty\":7}",
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":108,\"qty\":7}",
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":107,\"qty\":7}",
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":106,\"qty\":7}"
        };

        for (String body : sellBodies1) {
            this.mockMvc.perform(post("/order/dev")
                            .contentType("application/json")
                            .header("clientId", "client-11")
                            .content(body))
                    .andExpect(status().isOk());
            Tools.delay(10);
        }

        String[] buyBodies1 = {
                "{\"symbol\":\"dev\",\"tradeType\":\"BUY\",\"orderType\":\"LIMIT\",\"cost\":108,\"qty\":10}",
                "{\"symbol\":\"dev\",\"tradeType\":\"BUY\",\"orderType\":\"LIMIT\",\"cost\":107,\"qty\":10}",
                "{\"symbol\":\"dev\",\"tradeType\":\"BUY\",\"orderType\":\"LIMIT\",\"cost\":106,\"qty\":10}"
        };

        for (String body : buyBodies1) {
            this.mockMvc.perform(post("/order/dev")
                            .contentType("application/json")
                            .header("clientId", "client-21")
                            .content(body))
                    .andExpect(status().isOk());
            Tools.delay(10);
        }

        Tools.delay(500);

        String[] sellBodies2 = {
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":107,\"qty\":5}",
                "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":105,\"qty\":5}"
        };

        for (String body : sellBodies2) {
            this.mockMvc.perform(post("/order/dev")
                            .contentType("application/json")
                            .header("clientId", "client-12")
                            .content(body))
                    .andExpect(status().isOk());
            Tools.delay(10);
        }

        String[] buyBodies2 = {
                "{\"symbol\":\"dev\",\"tradeType\":\"BUY\",\"orderType\":\"LIMIT\",\"cost\":105,\"qty\":5}"
        };

        for (String body : buyBodies2) {
            this.mockMvc.perform(post("/order/dev")
                            .contentType("application/json")
                            .header("clientId", "client-22")
                            .content(body))
                    .andExpect(status().isOk());
            Tools.delay(10);
        }

        Tools.delay(500);

        List<ClientAccount> accountsDB = caRepo.findAll();
        List<ClientStocks> stocksDB = csRepo.findAll();
        List<CompletedTrades> ctList = ctRepo.findAll();

        List<ClientAccount> accounts = new ArrayList<>();
        accounts.add(new ClientAccount("client-11", 1491L));
        accounts.add(new ClientAccount("client-21", 2877L));
        accounts.add(new ClientAccount("client-22", 5000L));
        accounts.add(new ClientAccount("client-12", 632L));

        List<ClientStocks> stocks = new ArrayList<>();
        stocks.add(new ClientStocks(new ClientStocksId("client-11", "dev"), 86L));
        stocks.add(new ClientStocks(new ClientStocksId("client-12", "dev"), 94L));
        stocks.add(new ClientStocks(new ClientStocksId("client-21", "dev"), 20L));

        assertTrue(accountsDB.containsAll(accounts));
        assertTrue(stocksDB.containsAll(stocks));

        Map<Long, CompletedTrades> ctMap = ctList.stream()
                .collect(Collectors.toMap(CompletedTrades::getId, Function.identity()));

        assertEquals(ctMap.get(1L).getTradeType(), TradeType.SELL.toString());
        assertEquals(ctMap.get(1L).getClientId(), "client-11");
        assertEquals(ctMap.get(1L).getSymbol(), "dev");
        assertEquals(ctMap.get(1L).getCost(), 106);
        assertEquals(ctMap.get(1L).getQty(), 7);
        assertEquals(ctMap.get(1L).getSum(), 742);

        assertEquals(ctMap.get(2L).getTradeType(), TradeType.BUY.toString());
        assertEquals(ctMap.get(2L).getClientId(), "client-21");
        assertEquals(ctMap.get(2L).getSymbol(), "dev");
        assertEquals(ctMap.get(2L).getCost(), 106);
        assertEquals(ctMap.get(2L).getQty(), 7);
        assertEquals(ctMap.get(2L).getSum(), 742);

        assertEquals(ctMap.get(3L).getTradeType(), TradeType.SELL.toString());
        assertEquals(ctMap.get(3L).getClientId(), "client-11");
        assertEquals(ctMap.get(3L).getSymbol(), "dev");
        assertEquals(ctMap.get(3L).getCost(), 107);
        assertEquals(ctMap.get(3L).getQty(), 3);
        assertEquals(ctMap.get(3L).getSum(), 321);

        assertEquals(ctMap.get(4L).getTradeType(), TradeType.BUY.toString());
        assertEquals(ctMap.get(4L).getClientId(), "client-21");
        assertEquals(ctMap.get(4L).getSymbol(), "dev");
        assertEquals(ctMap.get(4L).getCost(), 107);
        assertEquals(ctMap.get(4L).getQty(), 3);
        assertEquals(ctMap.get(4L).getSum(), 321);

        assertEquals(ctMap.get(5L).getTradeType(), TradeType.SELL.toString());
        assertEquals(ctMap.get(5L).getClientId(), "client-11");
        assertEquals(ctMap.get(5L).getSymbol(), "dev");
        assertEquals(ctMap.get(5L).getCost(), 107);
        assertEquals(ctMap.get(5L).getQty(), 4);
        assertEquals(ctMap.get(5L).getSum(), 428);

        assertEquals(ctMap.get(6L).getTradeType(), TradeType.BUY.toString());
        assertEquals(ctMap.get(6L).getClientId(), "client-21");
        assertEquals(ctMap.get(6L).getSymbol(), "dev");
        assertEquals(ctMap.get(6L).getCost(), 107);
        assertEquals(ctMap.get(6L).getQty(), 4);
        assertEquals(ctMap.get(6L).getSum(), 428);

        assertEquals(ctMap.get(7L).getTradeType(), TradeType.SELL.toString());
        assertEquals(ctMap.get(7L).getClientId(), "client-12");
        assertEquals(ctMap.get(7L).getSymbol(), "dev");
        assertEquals(ctMap.get(7L).getCost(), 105);
        assertEquals(ctMap.get(7L).getQty(), 5);
        assertEquals(ctMap.get(7L).getSum(), 525);

        assertEquals(ctMap.get(8L).getTradeType(), TradeType.BUY.toString());
        assertEquals(ctMap.get(8L).getClientId(), "client-21");
        assertEquals(ctMap.get(8L).getSymbol(), "dev");
        assertEquals(ctMap.get(8L).getCost(), 105);
        assertEquals(ctMap.get(8L).getQty(), 5);
        assertEquals(ctMap.get(8L).getSum(), 525);

        assertEquals(ctMap.get(9L).getTradeType(), TradeType.SELL.toString());
        assertEquals(ctMap.get(9L).getClientId(), "client-12");
        assertEquals(ctMap.get(9L).getSymbol(), "dev");
        assertEquals(ctMap.get(9L).getCost(), 107);
        assertEquals(ctMap.get(9L).getQty(), 1);
        assertEquals(ctMap.get(9L).getSum(), 107);

        assertEquals(ctMap.get(10L).getTradeType(), TradeType.BUY.toString());
        assertEquals(ctMap.get(10L).getClientId(), "client-21");
        assertEquals(ctMap.get(10L).getSymbol(), "dev");
        assertEquals(ctMap.get(10L).getCost(), 107);
        assertEquals(ctMap.get(10L).getQty(), 1);
        assertEquals(ctMap.get(10L).getSum(), 107);
    }
}
