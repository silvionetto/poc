package com.silvionetto.ai;

public record PlaceOrderRequest(
        String symbol,
        String side,
        int quantity,
        String orderType,
        Double limitPrice,
        String timeInForce
) {}
