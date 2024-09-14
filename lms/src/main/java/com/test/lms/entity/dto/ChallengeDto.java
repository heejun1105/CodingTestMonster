package com.test.lms.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeDto {

    private String title;
    private String content;
    private int expPoints;
    private boolean close;

}
