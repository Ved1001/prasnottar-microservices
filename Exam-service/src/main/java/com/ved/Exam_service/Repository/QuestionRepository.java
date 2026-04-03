package com.ved.Exam_service.Repository;

import com.ved.Exam_service.Entity.Question;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByExamId(String examId);
}
