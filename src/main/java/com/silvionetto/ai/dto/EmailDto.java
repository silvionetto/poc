package com.silvionetto.ai.dto;

import java.time.Instant;
import java.util.List;

public class EmailDto {
    private String id;
    private String threadId;
    private String subject;
    private String from;
    private String to;
    private List<String> cc;
    private List<String> bcc;
    private String body;
    private String snippet;
    private Instant date;
    private List<String> labels;
    private boolean isRead;
    
    public EmailDto() {}
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getThreadId() {
        return threadId;
    }
    
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public List<String> getCc() {
        return cc;
    }
    
    public void setCc(List<String> cc) {
        this.cc = cc;
    }
    
    public List<String> getBcc() {
        return bcc;
    }
    
    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getSnippet() {
        return snippet;
    }
    
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
    
    public Instant getDate() {
        return date;
    }
    
    public void setDate(Instant date) {
        this.date = date;
    }
    
    public List<String> getLabels() {
        return labels;
    }
    
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
}
