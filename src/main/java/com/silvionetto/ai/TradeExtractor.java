package com.silvionetto.ai;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TradeExtractor {

    private final ChatClient chatClient;

    public TradeExtractor(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT).build();
    }

    public Trade extractTrade(String emailBody) {
        BeanOutputConverter<Trade> beanOutputConverter = new BeanOutputConverter<>(Trade.class);
        String format = beanOutputConverter.getFormat();
        String script = """
                Generate a Trade object from this {text} in this {format}.
                If the value is not in the text, do not add to the trade.
                Do not guess any value.
                RULES:
                The simbol is the name of the company/security.
                Side only can by "Buy" or "Sell".
                The "sourceEmail" field should be filled with the field "from" from the email.
                The "sourceEmail" should be only the value inside < > not the full name.
                """;
        PromptTemplate template = new PromptTemplate(script);
        Prompt prompt = template.create(Map.of("text", emailBody, "format", format));
        return beanOutputConverter.convert(chatClient.prompt(prompt).call().content());
    }
}
