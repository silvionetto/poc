package com.silvionetto.ai;

public record TradeInstruction(
        String symbol,
        String side,      // "BUY" or "SELL"
        int quantity,
        String orderType, // "MARKET" or "LIMIT"
        Double limitPrice,
        String timeInForce // e.g. "DAY"
) {}
