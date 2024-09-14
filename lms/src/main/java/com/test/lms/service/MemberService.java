package com.test.lms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Exp;
import com.test.lms.entity.Member;
import com.test.lms.entity.dto.ExpDto;
import com.test.lms.exception.DataNotFoundException;
import com.test.lms.repository.ExpRepository;
import com.test.lms.repository.MemberRepository;
import com.test.lms.repository.PersistentLoginsRepository;
import com.test.lms.repository.QuizAnswerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ExpRepository expRepository;
    private final PersistentLoginsRepository persistentLoginsRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    // 회원 가입
    public Member create(String username, String password, String nickname, String email) {
        
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member();

        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password)); 
        member.setNickname(nickname);
        member.setEmail(email);
        member.setCreateTime(LocalDateTime.now());

		if (username.equals("admin")) {
			member.setRole("ADMIN");
		} else {
			member.setRole("USER");
		}
		
        this.memberRepository.save(member);
        
        // 회원 가입 시 Exp 엔티티 생성
        Exp exp = new Exp();
        exp.setMember(member);
        exp.setExpPoints(0);  // 기본 경험치 0
        this.expRepository.save(exp);  
        
        return member;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다.: " + username));

//      return new User(member.getUsername(), member.getPassword(), new ArrayList<>()); // 유저 정보를 반환
        return User
			.withUsername(member.getUsername())
			.password(member.getPassword())
			.roles(member.getRole())
			.build();
	}

	public Member findByUsername(String username) {
		Optional<Member> Member = this.memberRepository.findByUsername(username);
		if (Member.isPresent()) {
			return Member.get();
		} else {
			throw new DataNotFoundException("회원정보를 찾을 수 없습니다.");
		}
	}
	
		// 랭크 업데이트
	   @Transactional
	    public void updateMemberRank(Long memberNum) {
	        Member member = memberRepository.findById(memberNum)
	            .orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	        
	        Exp exp = expRepository.findByMember(member)
	            .orElseThrow(() -> new DataNotFoundException("회원의 경험치를 찾을 수 없습니다."));
	        
	        int expPoints = exp.getExpPoints();
	        String newRank;

	        if (expPoints < 100) {
	            newRank = "bronze";
	        } else if (expPoints < 200) {
	            newRank = "silver";
	        } else if (expPoints < 300) {
	            newRank = "gold";
	        } else if (expPoints < 500) {
	        	newRank = "diamond";
	        } else {
	        	newRank = "master";
	        }
	        
	        member.setUserRank(newRank);
	        memberRepository.save(member);
	    }

	   	// 경험치 추가
	    @Transactional
	    public void addExpPoints(Long memberNum, int points) {
	        Member member = memberRepository.findById(memberNum)
	            .orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
	        
	        Exp exp = expRepository.findByMember(member)
	            .orElse(new Exp());

	        exp.setMember(member);
	        exp.setExpPoints(exp.getExpPoints() + points);
	        expRepository.save(exp);

	        updateMemberRank(memberNum);
	    }
	
	    // 회원 경험치 조회
	    public Exp findExpByMember(Member member) {
	        Optional<Exp> exp = expRepository.findByMember(member);
	        if (exp.isPresent())
	        	return exp.get();
	        else {
	        	Exp newExp = new Exp();
	        	newExp.setExpPoints(0);
	        	newExp.setMember(member);
	        	return expRepository.save(newExp);
	        }
	        	
	    }
	    
	    // 회원 중복 체크
	    public boolean existsByUsername(String username) {
	        return memberRepository.findByUsername(username).isPresent();
	    }
	    // 닉네임 중복 체크
	    public boolean existsByNickname(String nickname) {
	        return memberRepository.findByNickname(nickname).isPresent();
	    }
	    
	    // 비밀번호 변경
	    @Transactional
	    public void changePassword(String username, String newPassword, String confirmPassword) {

	        Member member = memberRepository.findByUsername(username)
	                .orElseThrow(() -> new DataNotFoundException("회원 정보를 찾을 수 없습니다."));

	        member.setPassword(passwordEncoder.encode(newPassword));
	        memberRepository.save(member);
	    }
	    
	    public List<ExpDto> getTop5MembersByExp() {
	    	Pageable topFive = PageRequest.of(0, 5);
	    	return memberRepository.findTop5MembersByExp(topFive);
	    }
	    
	    // 회원 탈퇴 
	    @Transactional
	    public void withdrawMember(String username, String password) {
	    	
	        Member member = memberRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

	        if (!passwordEncoder.matches(password, member.getPassword())) {
	            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
	        }
	        
	        // QuizAnswer 삭제
	        quizAnswerRepository.deleteByMember(member);
	        // Exp 삭제
	        expRepository.deleteByMember(member);
	        // RemberMe 삭제
	        persistentLoginsRepository.deleteByUsername(username);
	        
	        memberRepository.delete(member);

	     
	    }
	
}	


