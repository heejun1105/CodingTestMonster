package com.test.lms.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.lms.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("SELECT c FROM Challenge c ORDER BY c.createDate DESC")
    List<Challenge> findRecent5(Pageable pageable);
}
