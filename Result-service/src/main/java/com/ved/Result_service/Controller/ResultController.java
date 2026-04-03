package com.ved.Result_service.Controller;

import com.ved.Result_service.DTO.AnswerRequest;
import com.ved.Result_service.Entity.Result;
import com.ved.Result_service.Model.Question;
import com.ved.Result_service.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private ExamClient examClient; // Our Feign Client

    @Autowired
    private ResultRepository resultRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(@RequestBody AnswerRequest request) {

        // 1. VALIDATION: Check Attempt Limits
        int existingAttempts = resultRepository.countByUserIdAndExamId(request.getUserId(), request.getExamId());
        if (existingAttempts >= 3) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Error: You have already used your 3 attempts for this exam.");
        }

        // 2. FETCH: Get correct answers from Exam Service via Feign
        List<Question> actualQuestions = examClient.getQuestions(request.getExamId());

        // 3. LOGIC: Calculate the score
        int correctCount = 0;
        for (Question actual : actualQuestions) {
            for (var studentAns : request.getResponses()) {
                if (actual.getId().equals(studentAns.getQuestionId())) {
                    if (actual.getCorrectAnswer().equalsIgnoreCase(studentAns.getStudentAnswer())) {
                        correctCount++;
                    }
                }
            }
        }

        // 4. STATUS TRACKING: Determine Pass/Fail (e.g., 50% to pass)
        String finalStatus = (correctCount >= (actualQuestions.size() / 2)) ? "PASSED" : "FAILED";

        // 5. SAVE: Store the result in exam_result_db
        Result result = new Result();
        result.setUserId(request.getUserId());
        result.setExamId(request.getExamId());
        result.setScore(correctCount);
        result.setAttemptNumber(existingAttempts + 1);
        result.setStatus(finalStatus);

        return ResponseEntity.ok(resultRepository.save(result));
    }
}
