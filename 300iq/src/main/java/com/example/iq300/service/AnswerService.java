package com.example.iq300.service;

import com.example.iq300.domain.Answer;
import com.example.iq300.domain.Question;
import com.example.iq300.domain.User;
import com.example.iq300.exception.DataNotFoundException;
import com.example.iq300.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final NotificationService notificationService; // 같은 패키지라 import 없어도 됨

    public void create(Question question, String content, User author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        
        // [수정] 객체 비교 대신 ID 비교로 변경 (더 안전함)
        if (!question.getAuthor().getId().equals(author.getId())) {
            notificationService.create(
                question.getAuthor(), 
                "질문에 새로운 답변이 달렸습니다.", 
                "/question/detail/" + question.getId()
            );
        }
    }

    public Answer getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
}