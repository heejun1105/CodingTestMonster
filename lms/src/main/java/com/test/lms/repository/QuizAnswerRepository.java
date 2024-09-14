package com.test.lms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;
import com.test.lms.entity.QuizAnswer;


public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findByQuiz(Quiz quiz);
    List<QuizAnswer> findByMember(Member member);

    @Query("SELECT qa FROM QuizAnswer qa WHERE qa.member = :member AND qa.quiz = :quiz ORDER BY qa.solvedQuizTime DESC LIMIT 1")
    QuizAnswer findByMemberAndQuiz(@Param("member") Member member, @Param("quiz") Quiz quiz);

    @Query("SELECT DISTINCT qa.quiz FROM QuizAnswer qa WHERE qa.isCorrect = true")
    Page<Quiz> findDistinctQuizzesByCorrectAnswers(Pageable pageable);
    
    @Query("SELECT DISTINCT q FROM QuizAnswer q WHERE q.solvedQuizTime BETWEEN :start AND :end GROUP BY q.id, q.solvedQuizTime ORDER BY q.solvedQuizTime DESC")
    List<QuizAnswer> findTop5DistinctBySolvedQuizTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    // 특정 퀴즈에 대해 정답으로 제출된 횟수 조회
    long countByQuizAndOutput(Quiz quiz, String output);
    void deleteByMember(Member member);
    
    // 특정 회원의 맞춘 문제 조회
    @Query("SELECT qa.quiz, qa.solvedQuizTime FROM QuizAnswer qa WHERE qa.member.memberNum = :memberId AND qa.isCorrect = true")
    Page<Object[]> findCorrectQuizzesWithSolvedTimeByMember(@Param("memberId") Long memberId, Pageable pageable);
    // 특정 회원의 틀린 문제 조회
    @Query("SELECT qa.quiz, qa.solvedQuizTime FROM QuizAnswer qa WHERE qa.member.memberNum = :memberId AND qa.isCorrect = false")
    Page<Object[]> findIncorrectQuizzesWithSolvedTimeByMember(@Param("memberId") Long memberId, Pageable pageable);
    
    Optional<QuizAnswer> findByQuizAndMemberAndIsCorrect(Quiz quiz, Member member, boolean isCorrect);
    Optional<QuizAnswer> findByQuizAndMember(Quiz quiz, Member member);
//    @Query("SELECT DISTINCT qa.quiz FROM QuizAnswer qa WHERE qa.member.memberNum = :memberId AND qa.isCorrect = true")
//    Page<Quiz> findDistinctQuizIdsByMemberAndIsCorrectTrue(@Param("memberId") Long memberId, Pageable pageable);
}
