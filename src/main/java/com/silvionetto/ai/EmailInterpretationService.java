package com.silvionetto.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class EmailInterpretationService {

    private final ChatClient chatClient;

    public EmailInterpretationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public TradeInstruction interpret(TradeEmail email) {
        String userContent = """
            You are a trading assistant. Extract a single trade instruction from the email below.
            If no valid trade is present, respond with JSON where symbol is null.

            Email subject: %s
            Email body:
            %s
            """.formatted(email.subject(), email.body());

        return chatClient.prompt()
                .system("Return ONLY valid JSON matching the TradeInstruction schema.")
                .user(userContent)
                .call()
                .entity(TradeInstruction.class);
    }
}
