package com.test.lms.controller;

import org.springframework.ai.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.lms.entity.Notice;
import com.test.lms.entity.Quiz;
import com.test.lms.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    
    private final NoticeRepository noticeRepository;

    // @GetMapping("/list")
    // public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        
    //     Page<Notice> paging = this.quizService.getList(page);
    //     model.addAttribute("paging", paging);
    //     return "quiz/quizBoard";

    // }
}
