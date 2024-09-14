package com.test.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Challenge;
import com.test.lms.entity.dto.ChallengeDto;
import com.test.lms.exception.DataNotFoundException;
import com.test.lms.repository.ChallengeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    // 등록
    public Challenge createChallenge(ChallengeDto challengeDto) {
        Challenge challenge = new Challenge();

        challenge.setTitle(challengeDto.getTitle());
        challenge.setContent(challengeDto.getContent());
        challenge.setExpPoints(challengeDto.getExpPoints());
        challenge.setClose(false);

        return challengeRepository.save(challenge);
    }

    // 1개 조회
    public Challenge getOneChallenge(Long challId) {
        return challengeRepository.findById(challId).orElseThrow(() -> new DataNotFoundException("챌린지를 찾을 수 없습니다."));
    }

    // 최근 등록된 챌린지 5개 조회
    public List<Challenge> get5Challenge() {
        Pageable pageable = PageRequest.of(0, 5);
        return challengeRepository.findRecent5(pageable);
    }

    // 페이징 된 모든 챌린지
    public Page<Challenge> getAllPage(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return challengeRepository.findAll(pageable);
    }

    // 수정
    public Challenge updateChallenge(Long id, ChallengeDto challengeDto) {
        Challenge challenge = challengeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("챌린지를 찾을 수 없습니다."));
    
        // DTO에서 엔티티로 데이터 복사
        challenge.setTitle(challengeDto.getTitle());
        challenge.setContent(challengeDto.getContent());
        challenge.setExpPoints(challengeDto.getExpPoints());
        challenge.setClose(challengeDto.isClose());

        return challengeRepository.save(challenge);
    }

    // 삭제
    public void deleteChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("챌린지를 찾을 수 없습니다."));
        
        challengeRepository.delete(challenge);
    }
}
