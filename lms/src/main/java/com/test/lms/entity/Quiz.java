package com.test.lms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Quiz {

    //PK
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long quizId;

    //퀴즈 제목
    @Column(nullable=false)
    private String title;

    //퀴즈 내용
    @Column(nullable=false, columnDefinition = "LONGTEXT")
    private String content;

    //퀴즈의 코드
    @Column(nullable=false, columnDefinition = "LONGTEXT")
    private String correct;

    // 코드 실행 아웃풋
    @Column(nullable=false)
    private String output;

    @Column(nullable=false)
    private String quizRank;

    private LocalDateTime createDate;
    
    // 제출 카운트
    @Column(nullable=false)
    private int count = 0;

    //출력예시
    public String quizAnswer(){
        return "문제 : " + title + "정답 : " + correct;
    }
}
