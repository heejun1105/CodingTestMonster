package com.test.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.lms.entity.Exp;
import com.test.lms.entity.Member;

public interface ExpRepository extends JpaRepository<Exp, Long>{

	@Query("SELECT SUM(e.expPoints) FROM Exp e WHERE e.member = :member")
    Integer sumExpPointsByMember(@Param("member") Member member);

    @Query("SELECT SUM(e.expPoints) FROM Exp e WHERE e.member = :member")
    Integer findTotalExpByMember(@Param("member") Member member);

    Optional<Exp> findByMember(Member member);
    void deleteByMember(Member member);
}
