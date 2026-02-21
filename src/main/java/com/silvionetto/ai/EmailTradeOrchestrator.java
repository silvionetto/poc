package com.silvionetto.ai;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTradeOrchestrator {

    private final EmailReaderService emailReaderService;
    private final AiTradeAgent aiTradeAgent;

    public EmailTradeOrchestrator(EmailReaderService emailReaderService,
                                  AiTradeAgent aiTradeAgent) {
        this.emailReaderService = emailReaderService;
        this.aiTradeAgent = aiTradeAgent;
    }

    @Scheduled(fixedDelay = 60_000)
    public void pollAndProcessEmails() throws Exception {
        List<TradeEmail> emails = emailReaderService.fetchNewEmails();
        for (TradeEmail email : emails) {
            String result = aiTradeAgent.processEmailAndTrade(email);
            // log, persist audit, send confirmation email, etc.
            System.out.println("Processed email: " + result);
        }
    }
}
