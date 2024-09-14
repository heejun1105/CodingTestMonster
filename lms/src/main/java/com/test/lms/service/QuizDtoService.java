package com.test.lms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Quiz;
import com.test.lms.entity.dto.QuizDto;
import com.test.lms.repository.QuizAnswerRepository;
import com.test.lms.repository.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizDtoService {

		private final QuizRepository quizRepository;
		private final QuizAnswerRepository quizAnswerRepository;
		
		 // 퀴즈 목록 반환 시 정답률 포함
	    public Page<QuizDto> getList(int page) {
	        Pageable pageable = PageRequest.of(page, 10);
	        Page<Quiz> quizPage = quizRepository.findAll(pageable);
	
	        // Quiz 엔티티를 QuizDto로 변환하며 정답률을 포함
	        return quizPage.map(quiz -> {
	            long totalSubmissions = quiz.getCount();
	            long correctSubmissions = quizAnswerRepository.countByQuizAndOutput(quiz, quiz.getOutput().trim());
	            double correctRate = totalSubmissions == 0 ? 0.0 : ((double) correctSubmissions / totalSubmissions) * 100;

	            // correctRate를 String으로 변환하여 QuizDto 생성
	            String correctRateString = String.format("%.2f%%", correctRate); // 소수점 두 자리까지만 표시하고 % 붙이기

	            return new QuizDto(quiz.getQuizId(), quiz.getTitle(), quiz.getQuizRank(), quiz.getCount(), correctRateString, null);
	        });
	    }
	    
//	    // 퀴즈 정답률 계산
//	    public double calculateCorrectRate(Long quizId) {
//	        Quiz quiz = quizRepository.findById(quizId)
//	                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));
//
//	        // 전체 제출 횟수
//	        long totalSubmissions = quiz.getCount();
//
//	        // 정답 제출 횟수
//	        long correctSubmissions = quizAnswerRepository.countByQuizAndOutput(quiz, quiz.getCorrect().trim());
//
//	        // 정답률 계산 (퍼센트)
//	        if (totalSubmissions == 0) {
//	            return 0.0;  // 제출된 적이 없는 경우 0% 반환
//	        }
//	        return ((double) correctSubmissions / totalSubmissions) * 100;
//	    }
}
