package com.test.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.lms.entity.PersistentLogins;

public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, String> {
    Optional<PersistentLogins> findBySeries(String series);
    void deleteByUsername(String username);
}
