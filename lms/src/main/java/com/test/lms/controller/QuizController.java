package com.test.lms.controller;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.lms.entity.Quiz;
import com.test.lms.service.QuizAnswerService;
import com.test.lms.service.QuizService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    
    private final QuizService quizService;
    private final QuizAnswerService quizAnswerService;

    //퀴즈 리스트 보기
    @GetMapping("/quizBoard")
    public String quizBoard(Model model, @RequestParam(value="page", defaultValue="0")int page){
       
        //페이징 처리한 퀴즈 리스트를 가져옴
        Page<Quiz> paging = this.quizService.getList(page);
        model.addAttribute("paging", paging);
        return "quiz/quizBoard";
    }

    //퀴즈 상세내역
    @GetMapping("/detail/{quizId}")
    public String quizDetail(@PathVariable("quizId") Long quizId, Model model){
        
        Quiz quiz = quizService.getQuizById(quizId);
        model.addAttribute("quizDetail", quiz);

        return "quiz/quizDetail";
          
    }

    

    //postamapping 사용해서 1. 퀴즈만들기 기능 2. URL quizEdit으로 하고 수정은 quizId  @PathVariable어쩌구 써서 있으면 수정 없으면 등록되게
    
    //퀴즈 정답 제출 및 검증
    // //퀴즈 정답 제출 및 검증
    // @PostMapping("/submit")
    // public ResponseEntity<Boolean> submitQuiz(@RequestParam Long quizId, @RequestParam String answer, @RequestParam boolean isPublic, @RequestParam String userName){ 
        
    //     Boolean isCorrect = quizService.submitQuizAnswer(quizId, answer, isPublic, userName);

    //     return ResponseEntity.ok(isCorrect);
    //     //제출 시 정답 공개 여부 설정
    //     // quizService.submitAnswer(quizId, correct, isPublic, Id);
    //     // return ResponseEntity.ok("정답 제출 완료");
    // }

}
