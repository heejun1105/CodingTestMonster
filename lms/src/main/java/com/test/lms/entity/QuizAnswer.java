package com.test.lms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성한 코드
    @Column(nullable=false, columnDefinition = "LONGTEXT")
    private String answer;
    // 제출한 답안
    private String output;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="member_id")
    private Member member;

    //사용자가 푼 문제의 정답을 공개 또는 비공개 여부 확인
    @Column(nullable = false, name = "is_public")
    private boolean isPublic;

    //퀴즈 정답과 오답 모두 DB에 저장
    @Column(nullable = false, name = "is_correct")
    private boolean isCorrect;

    //문제를 푼 날짜를 기록 : 메인 페이지 일일퀴즈 랭킹 표기에 사용
    @Column(nullable = false)
    private LocalDateTime solvedQuizTime;

}