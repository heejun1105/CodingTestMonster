package com.test.lms.controller.rest;

import java.util.List;

import org.springframework.data.domain.Page;
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

import com.test.lms.entity.Notice;
import com.test.lms.entity.dto.NoticeDto;
import com.test.lms.repository.MemberRepository;
import com.test.lms.service.NoticeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class RestNoticeController {

    private final NoticeService noticeService;
    private final MemberRepository memberRepository;

    // 최근 5개 공지가져오기
    @GetMapping
    public ResponseEntity<List<Notice>> get5Notices() {
        List<Notice> notices = noticeService.get5Notices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getNotice(@PathVariable("noticeId") Long id) {
        Notice notice = noticeService.getOne(id);
        return ResponseEntity.ok(notice);
    }
    
    
    // 페이징 처리된 공지사항 리스트
    @GetMapping("/list")
    public ResponseEntity<Page<Notice>> getPagedNotices(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Notice> pagedNotices = noticeService.getList(page);
        return ResponseEntity.ok(pagedNotices);
    }

    @PostMapping("/create")
    public ResponseEntity<Notice> createNotice(@RequestBody @Valid NoticeDto noticeDto) {
        Notice newNotice = noticeService.create(noticeDto);
        return ResponseEntity.ok(newNotice);
    }

    //공지사항 수정(관리자만!)
    @PutMapping("/update/{noticeId}")
    public ResponseEntity<Notice> updateNotice(@PathVariable("noticeId") Long noticeId, @RequestBody NoticeDto noticeDto) {

        Notice updatedNotice = noticeService.updateNotice(noticeId, noticeDto);
        return ResponseEntity.ok(updatedNotice);
    }

    // 공지사항 삭제 (관리자만 접근 가능)
    @DeleteMapping("/delete/{noticeId}")
    public ResponseEntity<String> deleteNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.delete(noticeId);
        return ResponseEntity.ok("삭제 되었습니다.");
    }

    //공지사항 검색
    // @GetMapping("/search")
    // public ResponseEntity<List<Notice>> searchNotice(@RequestParam String keyword){

    // }
}