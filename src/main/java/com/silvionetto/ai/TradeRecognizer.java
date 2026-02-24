package com.silvionetto.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TradeRecognizer {
    private final ChatClient chatClient;

    public TradeRecognizer(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String analyse(String emailBody) {
        PromptTemplate template = new PromptTemplate("""
            Analyse this text: {email} and answer the question.
            Is this text a request for a trade, yes or no?
            RULES:
            Give me the answer in one word "YES" or "NO".
        """);
        Prompt prompt = template.create(Map.of("email", emailBody));
        return chatClient.prompt(prompt).call().content();
    }
}
