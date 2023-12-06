package com.esprow.interview.sklroman.stockmarket.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientStocksRepository extends JpaRepository<ClientStocks, ClientStocksId> {

}
