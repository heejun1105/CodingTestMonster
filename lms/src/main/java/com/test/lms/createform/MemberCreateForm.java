package com.test.lms.createform;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {

    @Size(min=3, max=15)
    @NotEmpty(message="ID를 입력해주세요.")
    private String username;
    
    @NotEmpty(message="비밀번호를 입력해주세요.")
    private String password;

    // @NotEmpty(message="비밀번호 확인를 입력해주세요.")
    // private String pwd2;

    @NotEmpty(message="닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message="이메일을 입력해주세요.")
    @Email
    private String email;

}
