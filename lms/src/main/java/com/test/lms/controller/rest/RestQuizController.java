package com.test.lms.controller.rest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.entity.Quiz;
import com.test.lms.entity.dto.QuizDto;
import com.test.lms.service.QuizAnswerService;
import com.test.lms.service.QuizDtoService;
import com.test.lms.service.QuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class RestQuizController {

    private final QuizService quizService;
    private final QuizAnswerService quizAnswerService;
    private final QuizDtoService quizDtoService;

    // 퀴즈 목록 API (페이징 처리)
    @GetMapping("/list")
    public ResponseEntity<Page<QuizDto>> quizBoard(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<QuizDto> paging = quizDtoService.getList(page);
        return ResponseEntity.ok(paging);
    }

    // 퀴즈 상세 정보 API
    @GetMapping("/detail/{quizId}")
    public ResponseEntity<Quiz> getQuizDetail(@PathVariable("quizId") Long quizId) {
        // 퀴즈 ID로 퀴즈 상세 정보 가져오기
        Quiz quiz = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quiz);
    }

    // 퀴즈 정답 제출 및 검증 API
    @PostMapping("/submit")
    public ResponseEntity<Boolean> submitQuizAnswer(
        @RequestParam("quizId") Long quizId,  // 퀴즈 ID
        @RequestParam("answer") String answer,  // 사용자가 작성한 코드
        @RequestParam("output") String output, // 사용자가 제출한 답변
        @RequestParam("isPublic") boolean isPublic,  // 정답 공개 여부
        @RequestParam("username") String username  // 사용자 ID
    ) {
        // 퀴즈 정답 제출 및 결과 메시지 반환
        String decodeAnswer = URLDecoder.decode(answer, StandardCharsets.UTF_8);
        String decodeOutput = URLDecoder.decode(output, StandardCharsets.UTF_8);
        
        boolean isCorrect = quizService.submitQuizAnswer(quizId, decodeAnswer, decodeOutput, isPublic, username);

        // 결과 메시지를 JSON 형식으로 반환
        return ResponseEntity.ok(isCorrect);
    }   //return 값을 true or false 로 반환할 수 있도록 수정    

    @GetMapping("/api/top5quizzes")
    public ResponseEntity<List<Quiz>> getTop5QuizzesOfDay(){

        List<Quiz> top5Quizzes = quizAnswerService.getTop5QuizzesOfDay();

        return ResponseEntity.ok(top5Quizzes);
    }
    
    @PutMapping("/edit/{quizId}")
    public ResponseEntity<String> editHistoryQuiz(@PathVariable("quizId") Long quizId, @RequestBody Quiz quiz) {
        
        quiz.setQuizId(quizId);
        quizService.updateQuiz(quiz);

        return ResponseEntity.ok("퀴즈 수정 완료! : ID " + quizId);
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editQuiz(@RequestBody Quiz quiz) {
        quizService.create(quiz.getTitle(), quiz.getContent(), quiz.getCorrect(), quiz.getQuizRank(), quiz.getOutput());
        return ResponseEntity.ok("퀴즈가 생성되었습니다.");
    }

    //퀴즈 삭제 기능
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteQuiz(@PathVariable("quizId") Long quizId){
        
        //quizId로 퀴즈 찾기
        Quiz quiz = quizService.getQuizById(quizId); 
        if (quiz != null) {
            quizService.delete(quiz);
            return ResponseEntity.ok("삭제 완료 되었습니다. ID: " + quizId);
        } else {
            return ResponseEntity.status(404).body("퀴즈를 찾을 수 없습니다. ID: " + quizId);
        }
    }

    @GetMapping("/counts")
    public List<Quiz> top5Quizzes() {
        return quizService.getTop5QuizzesByCount();
    }
    
    //퀴즈 검색
    @GetMapping("/search")
    public ResponseEntity<List<Quiz>> searchQuizzes(@RequestParam("keyword") String keyword, @RequestParam(value = "searchType", defaultValue = "OR") String searchType){

        List<Quiz> quizs;
        
        //제목, 내용 각각 검색
        if ("OR".equalsIgnoreCase(searchType)) {
            quizs = quizService.searchByTitleOrContent(keyword);
        }
        //제목 + 내용 검색
        else if ("AND".equalsIgnoreCase(searchType)) {
            quizs = quizService.searchByTitleAndContent(keyword);
        } else {
            return ResponseEntity.badRequest().body(null);  // 잘못된 searchType 처리
        }

        return ResponseEntity.ok(quizs);
    }
}

