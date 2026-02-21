package com.silvionetto.ai.controller;

import com.silvionetto.ai.dto.EmailDto;
import com.silvionetto.ai.service.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    
    @Autowired
    private GmailService gmailService;
    
    @GetMapping
    public ResponseEntity<List<EmailDto>> getEmails(
            @RequestParam(defaultValue = "10") int maxResults) {
        try {
            List<EmailDto> emails = gmailService.getEmails(maxResults);
            return ResponseEntity.ok(emails);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{messageId}")
    public ResponseEntity<EmailDto> getEmailById(@PathVariable String messageId) {
        try {
            EmailDto email = gmailService.getEmailById(messageId);
            return ResponseEntity.ok(email);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/unread")
    public ResponseEntity<List<EmailDto>> getUnreadEmails() {
        try {
            List<EmailDto> emails = gmailService.getUnreadEmails();
            return ResponseEntity.ok(emails);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<EmailDto>> searchEmails(@RequestParam String query) {
        try {
            List<EmailDto> emails = gmailService.searchEmails(query);
            return ResponseEntity.ok(emails);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
