package com.test.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.lms.entity.Member;
import com.test.lms.entity.dto.ExpDto;


public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberNum(Long memberNum);
    Optional<Member> findByNickname(String nickname);

    @Query("SELECT new com.test.lms.entity.dto.ExpDto(m.nickname, e.expPoints, m.userRank) " +
            "FROM Member m JOIN Exp e ON m.memberNum = e.member.memberNum " +
            "ORDER BY e.expPoints DESC")
    List<ExpDto> findTop5MembersByExp(Pageable pageable);
}
