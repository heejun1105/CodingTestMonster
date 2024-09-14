package com.test.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Exp {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expNum;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private Member member;

    @Column(nullable = false, length = 1000)
    private int expPoints;

}
