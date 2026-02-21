package com.silvionetto.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.ReceivedDateTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReaderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailReaderService.class);

    @Value("${email.imap.host:imap.gmail.com}")
    private String imapHost;

    @Value("${email.imap.port:993}")
    private String imapPort;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    public List<TradeEmail> fetchNewEmails() throws EmailReaderException {
        logger.info("Starting to fetch new emails from {}", imapHost);
        
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.host", imapHost);
        props.put("mail.imap.port", imapPort);
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.auth", "true");

        Session session = Session.getInstance(props);
        Store store = null;
        Folder inbox = null;

        try {
            store = session.getStore("imaps");
            store.connect(username, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            logger.debug("Found {} unread messages", messages.length);

            List<TradeEmail> result = new ArrayList<>();
            for (Message msg : messages) {
                try {
                    TradeEmail tradeEmail = convertToTradeEmail(msg);
                    result.add(tradeEmail);
                    logger.debug("Processed email from: {}", tradeEmail.from());
                } catch (Exception e) {
                    logger.warn("Failed to process email: {}", e.getMessage());
                }
            }

            logger.info("Successfully fetched {} trade emails", result.size());
            return result;

        } catch (AuthenticationFailedException e) {
            logger.error("Email authentication failed for user: {}", username);
            throw new EmailReaderException("Authentication failed: " + e.getMessage(), e);
        } catch (MessagingException e) {
            logger.error("Failed to connect to email server: {}", e.getMessage());
            throw new EmailReaderException("Connection failed: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching emails: {}", e.getMessage());
            throw new EmailReaderException("Unexpected error: " + e.getMessage(), e);
        } finally {
            closeResourcesQuietly(inbox, store);
        }
    }

    public List<TradeEmail> fetchEmailsSince(Date sinceDate) throws EmailReaderException {
        logger.info("Fetching emails since: {}", sinceDate);
        
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.host", imapHost);
        props.put("mail.imap.port", imapPort);
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.auth", "true");

        Session session = Session.getInstance(props);
        Store store = null;
        Folder inbox = null;

        try {
            store = session.getStore("imaps");
            store.connect(username, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            ReceivedDateTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GE, sinceDate);
            Message[] messages = inbox.search(dateTerm);
            logger.debug("Found {} messages since {}", messages.length, sinceDate);

            List<TradeEmail> result = new ArrayList<>();
            for (Message msg : messages) {
                try {
                    TradeEmail tradeEmail = convertToTradeEmail(msg);
                    result.add(tradeEmail);
                } catch (Exception e) {
                    logger.warn("Failed to process email: {}", e.getMessage());
                }
            }

            logger.info("Successfully fetched {} trade emails since {}", result.size(), sinceDate);
            return result;

        } catch (MessagingException e) {
            logger.error("Failed to fetch emails since date: {}", e.getMessage());
            throw new EmailReaderException("Failed to fetch emails: " + e.getMessage(), e);
        } finally {
            closeResourcesQuietly(inbox, store);
        }
    }

    private TradeEmail convertToTradeEmail(Message msg) throws Exception {
        String subject = msg.getSubject() != null ? msg.getSubject() : "";
        String body = extractEmailBody(msg);
        String from = msg.getFrom() != null ? Arrays.toString(msg.getFrom()) : "";
        Date sentDate = msg.getSentDate() != null ? msg.getSentDate() : new Date();

        return new TradeEmail(subject, body, from, sentDate);
    }

    private String extractEmailBody(Message msg) throws Exception {
        if (msg.isMimeType("text/plain")) {
            return msg.getContent().toString();
        } else if (msg.isMimeType("multipart/*")) {
            MimeMessage mimeMsg = (MimeMessage) msg;
            return mimeMsg.getContent().toString();
        } else {
            return msg.getContent().toString();
        }
    }

    private void closeResourcesQuietly(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (MessagingException e) {
            logger.warn("Failed to close email folder: {}", e.getMessage());
        }

        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            logger.warn("Failed to close email store: {}", e.getMessage());
        }
    }
}
