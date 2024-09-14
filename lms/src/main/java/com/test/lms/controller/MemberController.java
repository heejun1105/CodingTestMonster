package com.test.lms.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.lms.createform.MemberCreateForm;
import com.test.lms.entity.Exp;
import com.test.lms.entity.Member;
import com.test.lms.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;




@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }
    
    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "member/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/signup";
        }
     
        try {
        	memberService.create(memberCreateForm.getUsername(),
                             memberCreateForm.getPassword(),
                             memberCreateForm.getNickname(),
                             memberCreateForm.getEmail());
        } catch (IllegalArgumentException e) {
            bindingResult.reject("회원가입에 실패했습니다.", e.getMessage());
            return "member/signup";
        }

        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:/member/login";
    }	
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/index";
    }
    
    // 마이페이지 처리
    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal) {
        String username = principal.getName();
        Member member = memberService.findByUsername(username);
        Exp exp = memberService.findExpByMember(member);  // Exp 정보 가져오기

        model.addAttribute("member", member);
        model.addAttribute("exp", exp);

        return "mypage";
    }
    
    // 경험치 추가
    @PostMapping("/member/{memberNum}/addExp")
    public ResponseEntity<?> addExp(@PathVariable Long memberNum, @RequestParam int points) {
        memberService.addExpPoints(memberNum, points);
        return ResponseEntity.ok().build();
    }

}
