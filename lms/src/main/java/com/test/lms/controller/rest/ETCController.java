package com.test.lms.controller.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.lms.entity.Blog;
import com.test.lms.entity.Recommend;
import com.test.lms.service.ETCService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/etc")
@RequiredArgsConstructor
public class ETCController {

    private final ETCService etcService;
    
    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getBlogs() {
        List<Blog> blogs = etcService.get5Blogs();
        return ResponseEntity.ok(blogs);
    }
    
    @GetMapping("/recommends")
    public ResponseEntity<List<Recommend>> getRecommends() {
        List<Recommend> recommends = etcService.get5Recommendeds();
        return ResponseEntity.ok(recommends);
    }
    

}
