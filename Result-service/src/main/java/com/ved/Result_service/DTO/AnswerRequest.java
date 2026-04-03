package com.ved.Result_service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AnswerRequest {
    private Long userId;
    private String examId;
    private List<AnswerDTO> responses; // Each item contains Question ID and the Student's Answer
}

