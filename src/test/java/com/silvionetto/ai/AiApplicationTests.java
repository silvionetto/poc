package com.silvionetto.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ai.chat.client.ChatClient;

@SpringBootTest
class AiApplicationTests {

	@MockitoBean
	private ChatClient chatClient;

	@Test
	void contextLoads() {
	}

}
