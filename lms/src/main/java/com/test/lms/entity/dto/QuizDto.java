package com.test.lms.entity.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuizDto {

    private Long quizId;
    private String title;
    private String quizRank;
    // 맞춘 인원
    private int solvedBy;
    // 정답률
    private String correctRate;
    // 문제를 푼 날짜
    private LocalDateTime solvedQuizTime;
}	
