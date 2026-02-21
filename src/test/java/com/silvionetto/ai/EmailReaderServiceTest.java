package com.silvionetto.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    "email.imap.host=imap.gmail.com",
    "email.imap.port=993",
    "email.username=test@example.com",
    "email.password=testpassword"
})
class EmailReaderServiceTest {

    @Test
    void contextLoads() {
        EmailReaderService service = new EmailReaderService();
        assertNotNull(service);
    }
}
