package com.silvionetto.ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class TradeMcpTools {

    private final RestTemplate rest = new RestTemplate();

    @Tool(
            name = "create_trade",
            description = "Creates a trade in the Spring Boot backend"
    )

    public Trade createTrade(Trade trade) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Trade> request = new HttpEntity<>(trade, headers);

        ResponseEntity<Trade> response = rest.postForEntity(
                "http://localhost:8080/api/trades",
                request,
                Trade.class
        );

        return response.getBody();
    }
}

