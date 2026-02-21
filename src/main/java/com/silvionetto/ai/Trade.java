package com.silvionetto.ai;

public record Trade(
        int tradeId,
        String simbol,
        double quantity,
        double price,
        String side,
        String sourceEmail) {
}
