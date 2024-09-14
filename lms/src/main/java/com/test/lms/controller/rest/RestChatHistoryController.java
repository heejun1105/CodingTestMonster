package com.test.lms.controller.rest;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.entity.ChatHistory;
import com.test.lms.entity.Member;
import com.test.lms.service.ChatHistoryService;
import com.test.lms.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat-history")
public class RestChatHistoryController {

	private final ChatHistoryService chatHistoryService;
    private final MemberService memberService;
    
    //챗봇내역
    @GetMapping("/{quizId}")
    public ResponseEntity<List<ChatHistory>> getChatHistory(
            @PathVariable("quizId") Long quizId,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = memberService.findByUsername(authentication.getName());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("문제번호 : "+ quizId);
        log.info("로그인한 회원 : " + member.getUsername());
        List<ChatHistory> chatHistories = chatHistoryService.getChatHistory(member, quizId);

        if (chatHistories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        chatHistories.forEach(chatHistory -> {
            log.info("회원 질문 : " + chatHistory.getMemberContent());
            log.info("챗봇 응답 : " + chatHistory.getBotContent());
        });

        return ResponseEntity.ok(chatHistories);
    }
}

