package com.silvionetto.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class TradingToolsTest {

    @Autowired
    TradingTools tools;

    @Test
    void testPlaceOrder() {
        PlaceOrderRequest req = new PlaceOrderRequest(
                "AAPL", "BUY", 10, "MARKET", null, "DAY"
        );

        PlaceOrderResult result = tools.placeOrder(req);

        assertEquals("Status should be OK", "OK", result.status());
    }
}
