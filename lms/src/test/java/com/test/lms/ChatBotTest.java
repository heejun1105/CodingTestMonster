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
// public class ChatBotTest {
	
// 	@Autowired
//     private MockMvc mockMvc;
	
// 	 @Autowired
// 	    private WebTestClient webTestClient;

//     @Test
//     @WithMockUser(username = "test", roles = "USER")
//     public void testGenerateEndpoint() throws Exception {
//         Long quizId = 2L; // 데이터베이스에 존재하는 퀴즈 ID
//         String message = "이문제는 어떻게 시작하면좋을까?";

//         mockMvc.perform(MockMvcRequestBuilders.get("/ai/generate")
//                 .param("message", message)
//                 .param("quizId", quizId.toString()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.generation").exists());
//     }
  
    
    
// }


