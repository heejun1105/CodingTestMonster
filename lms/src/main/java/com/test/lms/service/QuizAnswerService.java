package com.test.lms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;
import com.test.lms.entity.QuizAnswer;
import com.test.lms.entity.dto.QuizDto;
import com.test.lms.exception.DataNotFoundException;
import com.test.lms.repository.MemberRepository;
import com.test.lms.repository.QuizAnswerRepository;
import com.test.lms.repository.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizAnswerService {

	private final QuizRepository quizRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final MemberRepository memberRepository;

    public List<QuizAnswer> getQuizAnswer(Long quizId){

    	Quiz quiz = quizRepository.findById(quizId).orElse(null);
    	
        //quiz가 null이면 빈 리스트 반환
        if(quiz == null){
            return List.of();
        }

        // 퀴즈 답변 목록 조회
        return quizAnswerRepository.findByQuiz(quiz);

    }    

    public QuizAnswer getOneByQuizMember(Long id, String username) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new DataNotFoundException("퀴즈가 존재하지 않습니다."));
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다.: " + username));

        return quizAnswerRepository.findByMemberAndQuiz(member, quiz);
    }

    //username 기반으로 사용자가 정답을 제출한 QuizAnswer목록 조회
    public List<QuizAnswer> getQuizAnswerByUserName(String username){

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다.: " + username));

        List<QuizAnswer> quizAnswers = quizAnswerRepository.findByMember(member);

        return quizAnswers;
    }

    //최근 풀이한 5개 문제 ( * 중복되는지 체크해볼것)
    public List<Quiz> getTop5QuizzesOfDay() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 시작 시간 (00:00)
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 오늘 끝 시간 (23:59:59)

        Pageable pageable = PageRequest.of(0, 5); // 첫 페이지에서 5개의 결과 가져오기
        List<QuizAnswer> quizAnswers = quizAnswerRepository.findTop5DistinctBySolvedQuizTime(startOfDay, endOfDay, pageable);
        List<Quiz> quizzes = new LinkedList<>();

        for (QuizAnswer quizAnswer : quizAnswers) {
            quizzes.add(quizAnswer.getQuiz());
        }

        return quizzes;
    }

    // isCorrect가 트루인 것만 가져오는 List<QuizAnswer>
    public Page<QuizDto> getCorrectQuizList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Quiz> quizPage = quizAnswerRepository.findDistinctQuizzesByCorrectAnswers(pageable);

        return quizPage.map(quiz -> {
            long totalSubmissions = quiz.getCount();
            long correctSubmissions = quizAnswerRepository.countByQuizAndOutput(quiz, quiz.getOutput().trim());
            double correctRate = totalSubmissions == 0 ? 0.0 : ((double) correctSubmissions / totalSubmissions) * 100;

            String correctRateString = String.format("%.2f%%", correctRate);

            return new QuizDto(quiz.getQuizId(), quiz.getTitle(), quiz.getQuizRank(), quiz.getCount(), correctRateString, null);
        });
    }
    
    // 특정 회원 맞춘문제 불러오기
    public Page<QuizDto> getMemberCorrectQuizList(Long memberNum, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Object[]> quizAnswerPage = quizAnswerRepository.findCorrectQuizzesWithSolvedTimeByMember(memberNum, pageable);

        return quizAnswerPage.map(result -> {
            Quiz quiz = (Quiz) result[0];
            LocalDateTime solvedQuizTime = (LocalDateTime) result[1]; // 맞춘 시간

            return new QuizDto(
                quiz.getQuizId(),
                quiz.getTitle(),
                quiz.getQuizRank(),
                quiz.getCount(),
                null, solvedQuizTime 
            );
        });
    }
    
    // 특정 회원 틀린문제 불러오기
    public Page<QuizDto> getMemberIncorrectQuizList(Long memberNum, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Object[]> quizAnswerPage = quizAnswerRepository.findIncorrectQuizzesWithSolvedTimeByMember(memberNum, pageable);

        return quizAnswerPage.map(result -> {
            Quiz quiz = (Quiz) result[0];
            LocalDateTime solvedQuizTime = (LocalDateTime) result[1]; // 틀린 시간

            return new QuizDto(
                quiz.getQuizId(),
                quiz.getTitle(),
                quiz.getQuizRank(),
                quiz.getCount(),
                null, solvedQuizTime 
            );
        });
    }
}
