package com.silvionetto.ai.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.RefreshTokenRequest;
import com.google.auth.oauth2.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GmailConfig {
    
    private static final String APPLICATION_NAME = "Spring Boot Gmail App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    
    @Autowired
    private GmailProperties gmailProperties;
    
    @Bean
    public Gmail gmailService() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        GoogleCredentials credentials = createCredentials();
        
        return new Gmail.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    private GoogleCredentials createCredentials() throws IOException {
        if (gmailProperties.getAccessToken() != null && gmailProperties.getRefreshToken() != null) {
            AccessToken accessToken = new AccessToken(gmailProperties.getAccessToken(), null);
            GoogleCredentials credentials = GoogleCredentials.create(accessToken);
            
            if (credentials.isExpired()) {
                RefreshTokenRequest tokenRequest = new RefreshTokenRequest()
                        .setRefreshToken(gmailProperties.getRefreshToken())
                        .setClientId(gmailProperties.getClientId())
                        .setClientSecret(gmailProperties.getClientSecret())
                        .setTokenServerUri(GoogleCredentials.getOauth2Credentials().getOauth2TokenServerUri());
                
                TokenResponse tokenResponse = tokenRequest.execute();
                credentials = GoogleCredentials.create(
                        new AccessToken(tokenResponse.getAccessToken(), null)
                );
            }
            
            return credentials;
        }
        
        throw new IOException("Gmail credentials not properly configured");
    }
}
