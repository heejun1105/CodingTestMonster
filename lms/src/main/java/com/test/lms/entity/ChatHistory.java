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
public class ChatHistory {


	 	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long chatHistoryNum;

	 	@Column(nullable = false, columnDefinition = "TEXT")
	 	private String botContent; //챗봇이 한 말

	 	@Column(nullable = false, columnDefinition = "TEXT")
	 	private String memberContent; //사용자가 한 말

	 	@Column(nullable = false, length = 100)
	 	private LocalDateTime createTime; // 생성날짜

	  @ManyToOne(cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "member_num")
	  private Member member;

	  @ManyToOne(cascade = CascadeType.REMOVE)
	  @JoinColumn(name = "quizId")
	  private Quiz quiz;

}
