package com.test.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.test.lms.entity.Blog;
import com.test.lms.entity.Recommend;

@Service
public class ETCService {

    public List<Blog> get5Blogs() {
        List<String> titles = new ArrayList<>();
        titles.add("AI, 당신의 직업을 빼앗아 갈까? ChatGPT의 등장으로 전문직도 위험!");
        titles.add("반도체 대란, 세계 경제를 마비시키다! 자동차부터 스마트폰까지 생산 중단 위기");
        titles.add("현실 세계는 이제 그만! 메타버스에 수조 원 쏟아붓는 IT 공룡들의 광기");
        titles.add("5G가 당신의 뇌를 조종한다? 초고속 네트워크의 숨겨진 위험");
        titles.add("당신의 모든 것이 노출된다! 끊이지 않는 사이버 공격, 개인정보 보호의 종말");

        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            Blog blog = new Blog();
            blog.setTitle(titles.get(i));
            blog.setLike(100 * (i + 1));
            blogs.add(blog);
        }

        return blogs;
    }

    public List<Recommend> get5Recommendeds() {
        List<Recommend> recommends = new ArrayList<>();
        recommends.add(new Recommend("AI 엔지니어", "테크 기업 A"));
        recommends.add(new Recommend("데이터 사이언티스트", "IT 기업 B"));
        recommends.add(new Recommend("클라우드 아키텍트", "소프트웨어 회사 C"));
        recommends.add(new Recommend("사이버 보안 전문가", "보안 기업 D"));
        recommends.add(new Recommend("UX/UI 디자이너", "스타트업 E"));
        
        return recommends;
    }
}
