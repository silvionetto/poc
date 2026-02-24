package com.silvionetto.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class EmailPollingService {

    private static final Logger logger = LoggerFactory.getLogger(EmailPollingService.class);
    private final GmailService gmailService;
    private final EmailSummarizer emailSummarizer;
    private final TradeRecognizer tradeRecognizer;
    private final TradeExtractor tradeExtractor;
    private final ObjectMapper mapper = new ObjectMapper();

    public EmailPollingService(GmailService gmailService, TradeExtractor tradeExtractor,
                               EmailSummarizer emailSummarizer, TradeRecognizer tradeRecognizer) {
        this.gmailService = gmailService;
        this.tradeExtractor = tradeExtractor;
        this.emailSummarizer = emailSummarizer;
        this.tradeRecognizer = tradeRecognizer;
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void pollEmails() {
        try {
            Gmail gmail = gmailService.getService();

            ListMessagesResponse response = gmail.users().messages()
                    .list("me")
                    .setQ("is:unread")
                    .setMaxResults(10L)
                    .execute();

            if (response.getMessages() == null) return;

            for (Message msg : response.getMessages()) {
                Message full = gmail.users().messages().get("me", msg.getId())
                        .setFormat("full")
                        .execute();

                Map<String, Object> json = extractJson(full);
                logger.debug("Extracted Email JSON: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

                String summary = emailSummarizer.summarize(json.get("body").toString());
                logger.debug("Summary: {}", summary);

                String isTrade = tradeRecognizer.analyse(summary);
                if (isTrade.equalsIgnoreCase("Yes")) {
                    String tradeMessage = mapper.writeValueAsString(json);
                    Trade trade = tradeExtractor.extractTrade(tradeMessage);
                    logger.info("Trade created: {}", trade);
                } else {
                    logger.info("No trade found for message ID: {}", msg.getId());
                }
            }

        } catch (Exception e) {
            logger.error("Error occurred while polling emails", e);
        }
    }

    private Map<String, Object> extractJson(Message message) {
        Map<String, Object> json = new HashMap<>();

        json.put("id", message.getId());
        json.put("threadId", message.getThreadId());

        String subject = message.getPayload().getHeaders().stream()
                .filter(h -> h.getName().equalsIgnoreCase("Subject"))
                .findFirst().map(MessagePartHeader::getValue).orElse("");

        String from = message.getPayload().getHeaders().stream()
                .filter(h -> h.getName().equalsIgnoreCase("From"))
                .findFirst().map(MessagePartHeader::getValue).orElse("");

        json.put("subject", subject);
        json.put("from", from);

        String body = extractBody(message.getPayload());
        body = cleanBody(body);
        json.put("body", body);

        return json;
    }

    private String cleanBody(String body) {
        return body.replaceAll("[^a-zA-Z0-9\\s.,:]", "");
    }

    private String extractBody(MessagePart part) {
        if (part.getBody() != null && part.getBody().getData() != null) {
            return new String(Base64.getUrlDecoder().decode(part.getBody().getData()));
        }

        if (part.getParts() != null) {
            for (MessagePart p : part.getParts()) {
                String result = extractBody(p);
                if (result != null) return result;
            }
        }

        return "";
    }
}
