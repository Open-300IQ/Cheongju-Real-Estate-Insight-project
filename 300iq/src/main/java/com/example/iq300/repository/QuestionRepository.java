package com.example.iq300.repository;

import com.example.iq300.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 자유게시판(Board)과 동일하게 페이징, 검색 기능 추가
    Page<Question> findAll(Pageable pageable);
    Page<Question> findBySubjectContaining(String kw, Pageable pageable);
    Page<Question> findByContentContaining(String kw, Pageable pageable);
}