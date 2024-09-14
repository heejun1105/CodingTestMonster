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
public class Notice {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noticeId;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable=false)
    private LocalDateTime createDate;

    private LocalDateTime updateDate; //수정일자
    
}
