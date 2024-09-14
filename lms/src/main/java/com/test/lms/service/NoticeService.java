package com.test.lms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.lms.entity.Notice;
import com.test.lms.entity.dto.NoticeDto;
import com.test.lms.repository.NoticeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지 하나 가져오기
    public Notice getOne(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 공지를 찾을 수 없습니다. ID : " + id));
    }
    
    //모든 공지 가져오기
    public List<Notice> getAllNotice(){
        return noticeRepository.findAll();
    }

     //페이징 처리
    public Page<Notice> getList(int page){
        Pageable pageable = PageRequest.of(page,5);
        return this.noticeRepository.findAll(pageable);
    }

    // 최근 등록된 챌린지 5개 조회
    public List<Notice> get5Notices() {
        Pageable pageable = PageRequest.of(0, 5);
        return noticeRepository.findRecent5(pageable);
    }

    //공지 작성
    public Notice create(NoticeDto noticeDto) {
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setCreateDate(LocalDateTime.now());

        return noticeRepository.save(notice);
    }

    //공지 수정
    public Notice updateNotice(Long id, NoticeDto noticeDto){

        Notice existingNotice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 공지를 찾을 수 없습니다. ID : " + id));
        existingNotice.setTitle(noticeDto.getTitle());
        existingNotice.setContent(noticeDto.getContent());

        return noticeRepository.save(existingNotice);
    }

    //공지 삭제
    public void delete(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 공지를 찾을 수 없습니다. ID : " + id));
        this.noticeRepository.delete(notice);
    }
    
}
