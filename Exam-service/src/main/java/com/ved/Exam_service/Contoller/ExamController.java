package com.ved.Exam_service.Contoller;

import com.ved.Exam_service.Entity.Question;
import com.ved.Exam_service.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ExamController {

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/add-question")
    public Question add(@RequestBody Question question) {
        return questionRepository.save(question);
    }

    @GetMapping("/get/{examId}") // Added "/get" to match your URL
    public List<Question> getQuestions(@PathVariable String examId) {
        List<Question> questions = questionRepository.findByExamId(examId);

        // Safety check: If no questions found, return an empty list instead of a crash
        if (questions == null) {
            return new java.util.ArrayList<>();
        }
        return questions;
    }
}
