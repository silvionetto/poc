package com.silvionetto.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class EmailSummarizer {

    private final ChatClient chatClient;

    EmailSummarizer(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String summarize(String emailBody) {
        PromptTemplate template = new PromptTemplate("Summarize the following email in 3–5 sentences:\n\n{email}");
        Prompt prompt = template.create(Map.of("email", emailBody));
        return chatClient.prompt(prompt).call().content();
    }
}
