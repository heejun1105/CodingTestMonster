package com.test.lms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.test.lms.service.QuizService;


@SpringBootTest
public class QuizTest {

    @Autowired
    private QuizService quizService;
    // private QuizRepository quizRepository;
    
    //게시판 페이징용 더미데이터 100개
    @Test
    void quizTest() {
        for (int i = 1; i <= 100; i++) {
            String quizRank = (i % 4 == 0) ? "A" : (i % 3 == 0) ? "B" : (i % 2 == 0) ? "C" : "D";
            String title = "기초 문제 " + i;
            String content = "자바 웹페이지 개발툴의 이름은? (" + i + ")";
            String correct = (i % 5 == 0) ? "Spring Boot" : "Spring Framework";
            String output = "";
            quizService.create(title, content, correct, quizRank, output);


        }
    }

    // @Test
    // void quizTest(){

    //     Quiz q1 = new Quiz();
        
    //     String quizRank = "D";
    //     String title = "기초 문제";
    //     String content = "자바 웹페이지 개발툴의 이름은?";
    //     String correct = "Spring Boot";
    //     quizService.create(title, content, correct, quizRank);

    // }
}
