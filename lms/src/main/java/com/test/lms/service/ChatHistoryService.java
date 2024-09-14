package com.test.lms.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.test.lms.entity.ChatHistory;
import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;
import com.test.lms.repository.ChatHistoryRepository;
import com.test.lms.repository.MemberRepository;
import com.test.lms.repository.QuizRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatHistoryService {

	private final MemberRepository memberRepository;
	private final QuizRepository quizRepository;
	private final ChatHistoryRepository chatHistoryRepository;
	private final QuizService quizService;


	//대화내역 생성
	public void ChatHistoryCreate(Member member, Quiz quiz, String memberContent, String botContent) {
		log.info("ChatHistoryCreate 메서드 시작");
		ChatHistory chatHistory = new ChatHistory();
		chatHistory.setMember(member);
		chatHistory.setQuiz(quiz);
		chatHistory.setBotContent(botContent);
		chatHistory.setMemberContent(memberContent);
		chatHistory.setCreateTime(LocalDateTime.now());


		this.chatHistoryRepository.save(chatHistory);
		log.info("ChatHistoryCreate 메서드 끝");

	}

	
	//대화내역 조회
	public List<ChatHistory> getChatHistory(Member member, Long quizId) {

		Quiz quiz = quizService.getQuizById(quizId);

		//log.debug("getChatHistory 메서드 시작: member={}, quizId={}", member.getUsername(), quizId);
        
        List<ChatHistory> chatHistories = chatHistoryRepository.findByMemberAndQuiz(member, quiz);
        
        if (chatHistories.isEmpty()) {
            log.debug("채팅 기록이 없습니다.");
            return Collections.emptyList();
        }
        
       // log.debug("getChatHistory 메서드 끝. 반환된 채팅 기록 수: {}", chatHistories.size());
        return chatHistories;
    }
	
	//대화내역 수정
	public void ChatHistoryUpdate(Member member, Quiz quiz, String memberContent, String botContent) {
		
		ChatHistory chatHistory = new ChatHistory();
		

		chatHistory.setMember(member);
		chatHistory.setQuiz(quiz);
		chatHistory.setBotContent(botContent);
		chatHistory.setMemberContent(memberContent);
		chatHistory.setCreateTime(LocalDateTime.now());


		this.chatHistoryRepository.save(chatHistory);

	}


	//대화내역 삭제
	public void ChatHistoryDelete(ChatHistory chatHistory) {
		this.chatHistoryRepository.delete(chatHistory);
	}

}