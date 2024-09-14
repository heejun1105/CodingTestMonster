package com.test.lms;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.test.lms.repository.QuizRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class GetChatHistoryTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private QuizRepository quizRepository;
	
	@Test
    @WithMockUser(username = "test", roles = "USER")
    public void testGetChatHistory() throws Exception {
        Long quizId = 2L;

        log.info("Starting testGetChatHistory");

        // 테스트 전 quizId의 존재 여부 확인
        boolean quizExists = quizRepository.existsById(quizId);
        log.info("Quiz with id {} exists: {}", quizId, quizExists);

        if (!quizExists) {
            log.warn("Quiz with id {} does not exist. Test may fail.", quizId);
        }

        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat-history/{quizId}", quizId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.chatHistoryNum").exists())
                    .andExpect(jsonPath("$.botContent").exists())
                    .andExpect(jsonPath("$.memberContent").exists())
                    .andDo(print())
                    .andReturn();

            log.info("Response: " + result.getResponse().getContentAsString());
            log.info("Completed testGetChatHistory successfully");
        } catch (Exception e) {
            log.error("Error occurred during testGetChatHistory", e);
            throw e;  // 예외를 다시 던져서 테스트 실패를 표시
        }
    }
}