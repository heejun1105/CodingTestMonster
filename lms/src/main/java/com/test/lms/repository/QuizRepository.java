package com.test.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.lms.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    //페이징 처리
    Page<Quiz> findAll(Pageable pageable);

    @Query("SELECT q FROM Quiz q ORDER BY q.count DESC")
    List<Quiz> findTop5QuizzesByCountDesc(Pageable pageable);

    //제목 또는 내용으로 검색
    List<Quiz> findByTitleContainingOrContentContaining(String title, String content);
    
    //제목, 내용 모두 포함해서 검색
    List<Quiz> findByTitleContainingAndContentContaining(String title, String content);
}
