package com.test.lms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.test.lms.entity.QuizAnswer;
import com.test.lms.service.QuizAnswerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QuizAnwerController {

    private final QuizAnswerService quizAnswerService;

    //QuizAnswer 목록 조회 후 뷰에 전달
    @GetMapping("/quiz/{quizId}/answers")
    public String getQuizAnswers(@PathVariable Long quizId, Model model){

        List<QuizAnswer> quizAnswers = quizAnswerService.getQuizAnswer(quizId);
        model.addAttribute("quizAnswers", quizAnswers);

        return "quizAnswers";
        
    }
    

}
