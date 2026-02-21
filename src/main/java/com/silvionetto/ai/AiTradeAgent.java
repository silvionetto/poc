package com.silvionetto.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiTradeAgent {

    private final ChatClient chatClient;

    public AiTradeAgent(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String processEmailAndTrade(TradeEmail email) {
        String content = """
            You are an automated trading agent.
            1. Read the email.
            2. If it contains a clear trade instruction, call the `place_order` tool.
            3. If not, explain why no trade was executed.

            Email subject: %s
            Email body:
            %s
            """.formatted(email.subject(), email.body());

        return chatClient.prompt()
                .system("You have access to tools from the trading MCP server. Use them when appropriate.")
                .user(content)
                .call()
                .content(); // will include tool calls + final answer
    }
}
