package com.silvionetto.ai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @PostMapping
    public ResponseEntity<Trade> createTrade(@RequestBody Trade trade) {
        // persist or process trade
        System.out.println("Trade created: " + trade);
        return ResponseEntity.ok(trade);
    }
}
