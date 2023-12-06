package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.data.*;
import com.esprow.interview.sklroman.stockmarket.error.ClientFundsAccountIsNotExistException;
import com.esprow.interview.sklroman.stockmarket.error.ClientStocksAccountIsNotExistException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountValidator {

    private final ClientAccountRepository caRepo;
    private final ClientStocksRepository csRepo;

    public AccountValidator(ClientAccountRepository caRepo, ClientStocksRepository csRepo) {
        this.caRepo = caRepo;
        this.csRepo = csRepo;
    }

    public void checkAccount(String clientId) {
        Optional<ClientAccount> account = caRepo.findById(clientId);
        if (account.isEmpty()) {
            throw new ClientFundsAccountIsNotExistException(clientId);
        }
    }

    public void checkStocks(String clientId, String symbol) {
        Optional<ClientStocks> stocks = csRepo.findById(new ClientStocksId(clientId, symbol));
        if (stocks.isEmpty()) {
            throw new ClientStocksAccountIsNotExistException(clientId);
        }
    }
}
