package com.test.lms.createform;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizCreateForm {

    @NotEmpty(message = "퀴즈 제목을 입력 해 주세요.")
    private String title;

    @NotEmpty(message = "퀴즈 내용을 입력 해 주세요.")
    private String content;

    @NotEmpty(message = "정답은 필수 입력 항목입니다.")
    private String correct;

    
    
}   
