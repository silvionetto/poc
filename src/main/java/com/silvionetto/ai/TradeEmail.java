package com.silvionetto.ai;

import java.util.Date;

public record TradeEmail(
        String subject,
        String body,
        String from,
        Date sentAt
) {}
