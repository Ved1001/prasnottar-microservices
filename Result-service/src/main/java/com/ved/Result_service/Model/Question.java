package com.ved.Result_service.Model;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String content;
    private String correctAnswer;
}
