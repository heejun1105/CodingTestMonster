package com.test.lms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.lms.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    //페이징 처리
    Page<Notice> findAll(Pageable pageable);

    @Query("SELECT n FROM Notice n ORDER BY n.createDate DESC")
    List<Notice> findRecent5(Pageable pageable);
    
}
