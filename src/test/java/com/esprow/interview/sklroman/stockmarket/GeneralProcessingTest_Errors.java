package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.data.ClientAccountRepository;
import com.esprow.interview.sklroman.stockmarket.data.ClientStocksRepository;
import com.esprow.interview.sklroman.stockmarket.data.CompletedTradesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GeneralProcessingTest_Errors {

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
    }

    @Test
    void clientHasntStockAccount() {

        String body = "{\"symbol\":\"dev\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":109,\"qty\":7}";

        try {
            this.mockMvc.perform(post("/order/dev")
                    .contentType("application/json")
                    .header("clientId", "client-11")
                    .content(body));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("Client, ID:client-11 hasn't stock account"));
        }
    }

    @Test
    void wrongSymbol() {

        String body = "{\"symbol\":\"dev1\",\"tradeType\":\"SELL\",\"orderType\":\"LIMIT\",\"cost\":109,\"qty\":7}";

        try {
            this.mockMvc.perform(post("/order/dev2")
                    .contentType("application/json")
                    .header("clientId", "client-11")
                    .content(body));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("The symbol in the body and the path must be the same"));
        }
    }
}
