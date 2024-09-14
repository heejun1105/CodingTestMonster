// package com.test.lms;

// import org.junit.jupiter.api.Test;
// import org.springframework.ai.chat.model.ChatResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.reactive.server.WebTestClient;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// import reactor.test.StepVerifier;

// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class ChatbotStreamTest {

// 	@Autowired
//     private MockMvc mockMvc;
	
// 	 @Autowired
// 	    private WebTestClient webTestClient;

   
//    @Test
//    @WithMockUser(username = "test", roles = "USER")
//    public void testGenerateStreamEndpoint() {
//        Long quizId = 2L; // 데이터베이스에 존재하는 퀴즈 ID
//        String message = "이문제는 어떻게 시작하면 좋을까!?";

//        webTestClient.get().uri(uriBuilder -> uriBuilder
//                .path("/ai/generateStream")
//                .queryParam("message", message)
//                .queryParam("quizId", quizId)
//                .build())
//            .exchange()
//            .expectStatus().isOk()
//            .expectHeader().contentType("application/stream+json")
//            .returnResult(ChatResponse.class)
//            .getResponseBody()
//            .as(StepVerifier::create)
//            .expectNextCount(1) // 최소한 하나의 응답이 있어야 함
//            .thenCancel()
//            .verify();
//    }
    
    
// }

