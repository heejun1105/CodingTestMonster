package com.test.lms.service;

import org.springframework.stereotype.Service;

import com.test.lms.entity.Exp;
import com.test.lms.entity.Member;
import com.test.lms.repository.ExpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpService {
	
    private final ExpRepository expRepository;
    private final MemberService memberService;
    
    
}
