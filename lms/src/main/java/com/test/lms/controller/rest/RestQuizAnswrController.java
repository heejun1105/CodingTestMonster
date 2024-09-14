package com.test.lms.controller.rest;

import java.util.List;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;
import com.test.lms.entity.QuizAnswer;
import com.test.lms.entity.dto.QuizDto;
import com.test.lms.service.QuizAnswerService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizAnswer")
public class RestQuizAnswrController {

    private final QuizAnswerService quizAnswerService;
    
    //특정 퀴즈에 대한 QuizAnswer 목록을 JSON 으로 반환
    @GetMapping("/{quizId}/answers")
    public ResponseEntity<List<QuizAnswer>> getQuizAnswer(@PathVariable("quizId") Long quizId){

        //QuizAnswer 목록 조회
        List<QuizAnswer> quizAnswers = quizAnswerService.getQuizAnswer(quizId);

        //JSON 으로 QuizAnswer 목록 반환
        return ResponseEntity.ok(quizAnswers);
    }

    // 특정 퀴즈에서 사용자의 아이디와 함께 최근 작성한 QuizAnswer 작성
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizAnswer> getOneQuizAnswer(@PathVariable("quizId") Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        QuizAnswer quizAnswer = quizAnswerService.getOneByQuizMember(quizId, authentication.getName());

        return ResponseEntity.ok(quizAnswer);
    }
    

    // 정답이 공개된 문제들
    @GetMapping("/solved")
    public ResponseEntity<Page<QuizDto>> solvedQuiz(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<QuizDto> quizzes = quizAnswerService.getCorrectQuizList(page);
        return ResponseEntity.ok(quizzes);
    }
    

    //사용자가 맞춘 퀴즈들을 마이페이지에 표시하는 기능 
    @GetMapping("/answers")
    public ResponseEntity<List<QuizAnswer>> getQuizAnswerByUserName(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //인증된 사용자의 이름 가져오기
        String userName = authentication.getName();  

        List<QuizAnswer> quizAnswers = quizAnswerService.getQuizAnswerByUserName(userName);

        return ResponseEntity.ok(quizAnswers);
    }

    @GetMapping("/top5")
    public List<Quiz>  getTop5QuizzesOfDay(){

        return quizAnswerService.getTop5QuizzesOfDay();
    }
    
    
}
