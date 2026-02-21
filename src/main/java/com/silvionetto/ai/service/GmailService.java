package com.silvionetto.ai.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.silvionetto.ai.dto.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(GmailService.class);
    private static final String USER_ID = "me";
    
    @Autowired
    private Gmail gmail;
    
    public List<EmailDto> getEmails(int maxResults) throws IOException {
        ListMessagesResponse response = gmail.users().messages()
                .list(USER_ID)
                .setMaxResults((long) maxResults)
                .execute();
        
        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        
        return messages.stream()
                .map(message -> {
                    try {
                        return getEmailDetails(message.getId());
                    } catch (IOException e) {
                        logger.error("Error fetching email details for ID: {}", message.getId(), e);
                        return null;
                    }
                })
                .filter(email -> email != null)
                .collect(Collectors.toList());
    }
    
    public EmailDto getEmailById(String messageId) throws IOException {
        return getEmailDetails(messageId);
    }
    
    public List<EmailDto> getUnreadEmails() throws IOException {
        ListMessagesResponse response = gmail.users().messages()
                .list(USER_ID)
                .setQ("is:unread")
                .execute();
        
        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        
        return messages.stream()
                .map(message -> {
                    try {
                        return getEmailDetails(message.getId());
                    } catch (IOException e) {
                        logger.error("Error fetching email details for ID: {}", message.getId(), e);
                        return null;
                    }
                })
                .filter(email -> email != null)
                .collect(Collectors.toList());
    }
    
    public List<EmailDto> searchEmails(String query) throws IOException {
        ListMessagesResponse response = gmail.users().messages()
                .list(USER_ID)
                .setQ(query)
                .execute();
        
        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        
        return messages.stream()
                .map(message -> {
                    try {
                        return getEmailDetails(message.getId());
                    } catch (IOException e) {
                        logger.error("Error fetching email details for ID: {}", message.getId(), e);
                        return null;
                    }
                })
                .filter(email -> email != null)
                .collect(Collectors.toList());
    }
    
    private EmailDto getEmailDetails(String messageId) throws IOException {
        Message message = gmail.users().messages()
                .get(USER_ID, messageId)
                .setFormat("full")
                .execute();
        
        EmailDto email = new EmailDto();
        email.setId(message.getId());
        email.setThreadId(message.getThreadId());
        email.setSnippet(message.getSnippet());
        email.setDate(Instant.ofEpochMilli(message.getInternalDate()));
        email.setLabels(message.getLabelIds());
        email.setRead(!message.getLabelIds().contains("UNREAD"));
        
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            switch (header.getName().toLowerCase()) {
                case "subject":
                    email.setSubject(header.getValue());
                    break;
                case "from":
                    email.setFrom(header.getValue());
                    break;
                case "to":
                    email.setTo(header.getValue());
                    break;
                case "cc":
                    email.setCc(List.of(header.getValue().split(",")));
                    break;
                case "bcc":
                    email.setBcc(List.of(header.getValue().split(",")));
                    break;
            }
        }
        
        email.setBody(getEmailBody(message.getPayload()));
        
        return email;
    }
    
    private String getEmailBody(MessagePart messagePart) {
        if (messagePart.getMimeType() != null && messagePart.getMimeType().startsWith("text/plain") && messagePart.getBody().getSize() > 0) {
            return new String(Base64.getDecoder().decode(messagePart.getBody().getData()));
        }
        
        if (messagePart.getMimeType() != null && messagePart.getMimeType().startsWith("multipart/")) {
            if (messagePart.getParts() != null) {
                for (MessagePart part : messagePart.getParts()) {
                    String body = getEmailBody(part);
                    if (body != null && !body.isEmpty()) {
                        return body;
                    }
                }
            }
        }
        
        return "";
    }
}
