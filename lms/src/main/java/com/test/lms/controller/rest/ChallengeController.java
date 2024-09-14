package com.test.lms.controller.rest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.entity.Challenge;
import com.test.lms.entity.dto.ChallengeDto;
import com.test.lms.service.ChallengeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<Challenge> createChallenge(@RequestBody @Valid ChallengeDto challengeDto) {
        Challenge createdChallenge = challengeService.createChallenge(challengeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChallenge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Challenge> getChallenge(@PathVariable("id") Long id) {
        Challenge challenge = challengeService.getOneChallenge(id);
        return ResponseEntity.ok(challenge);
    }

    @GetMapping
    public ResponseEntity<Page<Challenge>> getAllChallengeWithPaging(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Challenge> challenges = challengeService.getAllPage(page);
        return ResponseEntity.ok(challenges);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable("id") Long id, @RequestBody ChallengeDto challengeDto) {
        Challenge updatedChallenge = challengeService.updateChallenge(id, challengeDto);
        return ResponseEntity.ok(updatedChallenge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable("id") Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top5")
    public ResponseEntity<List<Challenge>> get5Challenges() {
        return ResponseEntity.ok(challengeService.get5Challenge());
    }
    
}
