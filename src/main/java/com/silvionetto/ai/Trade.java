package com.silvionetto.ai;

public record Trade(
        String tradeId,
        String simbol,
        double quantity,
        double price,
        String side,
        String sourceEmail) {
}
