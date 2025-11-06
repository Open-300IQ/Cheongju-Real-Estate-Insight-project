package com.example.iq300.service;

import com.example.iq300.domain.Question;
import com.example.iq300.domain.User;
import com.example.iq300.exception.DataNotFoundException;
import com.example.iq300.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * Q&A 게시판 목록 조회 (페이징, 검색, 정렬 기능)
     */
    public Page<Question> getPage(int page, String kw, String searchType, String sortType) {
        
        // 1. 정렬(Sort) 기준 설정
        List<Sort.Order> sorts = new ArrayList<>();
        if ("popular".equals(sortType)) {
            // (참고) '인기순'은 나중에 '답변 개수(answerList.size)' 등으로 구현 필요
            sorts.add(Sort.Order.desc("createDate")); // 임시
        } else {
            sorts.add(Sort.Order.desc("createDate")); // '최신순'
        }

        // 2. 페이징 설정 (페이지당 10개)
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 3. 검색(kw) 및 조회
        if (kw == null || kw.trim().isEmpty()) {
            return this.questionRepository.findAll(pageable);
        } else if ("subject".equals(searchType)) {
            return this.questionRepository.findBySubjectContaining(kw, pageable);
        } else if ("content".equals(searchType)) {
            return this.questionRepository.findByContentContaining(kw, pageable);
        } else {
            return this.questionRepository.findBySubjectContaining(kw, pageable); // 기본값: 제목
        }
    }
    
    /**
     * 질문 상세 조회
     */
    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    /**
     * 질문 생성
     */
    public void create(String subject, String content, User author) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(author); 
        this.questionRepository.save(q);
    }
}