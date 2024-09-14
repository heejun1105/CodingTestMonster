package com.test.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.lms.entity.ChatHistory;
import com.test.lms.entity.Member;
import com.test.lms.entity.Quiz;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {


	
    List<ChatHistory> findByMemberAndQuiz(Member member, Quiz quiz);
}

	

