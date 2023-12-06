package com.esprow.interview.sklroman.stockmarket.dev;

import com.esprow.interview.sklroman.stockmarket.ws.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("dev")
public class DevController {

    private final Logger logger = LogManager.getLogger();

    private final WebSocketService wss;

    public DevController(WebSocketService wss) {
        this.wss = wss;
    }

    @GetMapping("/{clientId}")
    public void dev(@PathVariable String clientId) {
        wss.sendMsg(clientId, "Хелло, Балаклава...");
    }


}
