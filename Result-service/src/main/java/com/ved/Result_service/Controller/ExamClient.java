package com.ved.Result_service.Controller;

import com.ved.Result_service.Model.Question;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "EXAM-SERVICE")
public interface ExamClient {

    @GetMapping("/exams/get/{examId}")
    public List<Question> getQuestions(@PathVariable("examId") String examId);
}
