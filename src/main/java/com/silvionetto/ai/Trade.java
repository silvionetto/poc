package com.silvionetto.ai;

public record Trade(
        String tradeId,
        String simbol,
        double quantity,
        double price,
        String currency,
        String side,
        String sourceEmail) {
}
