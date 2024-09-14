package com.test.lms.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.createform.MemberCreateForm;
import com.test.lms.entity.Exp;
import com.test.lms.entity.Member;
import com.test.lms.entity.dto.ExpDto;
import com.test.lms.entity.dto.PasswordChangeRequest;
import com.test.lms.entity.dto.QuizDto;
import com.test.lms.service.MemberService;
import com.test.lms.service.QuizAnswerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class RestMemberController {

    private final MemberService memberService;
    private final QuizAnswerService quizAnswerService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        log.info("Signup request received for email: {}", memberCreateForm.getEmail());
        
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred during signup");
            return ResponseEntity.badRequest()
                                .body(Map.of("error", "입력값이 올바르지 않습니다."));
        }

        try {
            memberService.create(memberCreateForm.getUsername(),
                                memberCreateForm.getPassword(),
                                memberCreateForm.getNickname(),
                                memberCreateForm.getEmail());
            log.info("User successfully signed up: {}", memberCreateForm.getUsername());
            return ResponseEntity.ok()
                                .body(Map.of("message", "회원가입이 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            log.error("Error during user creation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during signup", e);
            return ResponseEntity.internalServerError()
                                .body(Map.of("error", "회원가입 처리 중 오류가 발생했습니다."));
        }
    }
    
    @GetMapping("/check")
    public Map<String, Object> checkLoginStatus() {
        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberService.findByUsername(authentication.getName());
        if (authentication != null && authentication.isAuthenticated()) {
            response.put("loggedIn", true);
            response.put("username", authentication.getName()); // 사용자 이름을 반환
            response.put("nickname", member.getNickname());
            response.put("role", member.getRole());
        } else {
            response.put("loggedIn", false);
        }

        return response;
    }
    
    @GetMapping("/mypage")
    public ResponseEntity<?> getMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Member member = memberService.findByUsername(username);
        Exp exp = memberService.findExpByMember(member);
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", member.getUsername());
        response.put("nickname", member.getNickname());
        response.put("email", member.getEmail());
        response.put("rank", member.getUserRank());
        response.put("expPoints", exp.getExpPoints());

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameDuplicate(@RequestParam("username") String username) {
        boolean isDuplicate = memberService.existsByUsername(username);
        
        if (isDuplicate) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미 사용 중인 아이디입니다."));
        } else {
            return ResponseEntity.ok().body(Map.of("message", "사용 가능한 아이디입니다."));
        }
    }
    
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNicknameDuplicate(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = memberService.existsByNickname(nickname);
        
        if (isDuplicate) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미 사용 중인 닉네임입니다."));
        } else {
            return ResponseEntity.ok().body(Map.of("message", "사용 가능한 닉네임입니다."));
        }
    }

    
    // 비밀번호 변경 
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        memberService.changePassword(request.getUsername(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


    @GetMapping("/exp-top")
    public List<ExpDto> top5Exp() {
        return memberService.getTop5MembersByExp();
    }
    
    // 회원 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMember(@RequestBody Map<String, String> request,
    										HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String password = request.get("password");

        try {
            // 사용자의 비밀번호를 확인한 후 탈퇴 처리
            memberService.withdrawMember(username, password);
            
            // 로그아웃 처리
            SecurityContextHolder.clearContext();
            httpServletRequest.getSession().invalidate();  // 세션 무효화
            
            // JSESSIONID 쿠키 삭제
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 쿠키 만료
            httpServletResponse.addCookie(cookie);
            
            // 응답으로 성공 메시지 반환
            return ResponseEntity.ok().body(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("회원 탈퇴 처리 중 오류 발생", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "회원 탈퇴 처리 중 오류가 발생했습니다."));
        }
    }
    
    // mypage 맞춘 문제 가져오기
    @GetMapping("/my-correct-quizs")
    public ResponseEntity<Map<String, Object>> getCorrectQuizzesForMember(
            @RequestParam(value = "page", defaultValue = "0") int page) {

        // 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }

        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

        // 특정 회원이 맞춘 문제만 페이징 처리하여 반환
        Page<QuizDto> correctQuizzes = quizAnswerService.getMemberCorrectQuizList(member.getMemberNum(), page);

        Map<String, Object> response = new HashMap<>();
        response.put("content", correctQuizzes.getContent());
        response.put("totalPages", correctQuizzes.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    // mypage 틀린 문제 가져오기
    @GetMapping("/my-incorrect-quizs")
    public ResponseEntity<Map<String, Object>> getIncorrectQuizzesForMember(
            @RequestParam(value = "page", defaultValue = "0") int page) {

        // 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }

        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

        // 특정 회원이 틀린 문제만 페이징 처리하여 반환
        Page<QuizDto> incorrectQuizzes = quizAnswerService.getMemberIncorrectQuizList(member.getMemberNum(), page);

        Map<String, Object> response = new HashMap<>();
        response.put("content", incorrectQuizzes.getContent());
        response.put("totalPages", incorrectQuizzes.getTotalPages());

        return ResponseEntity.ok(response);
    }
}