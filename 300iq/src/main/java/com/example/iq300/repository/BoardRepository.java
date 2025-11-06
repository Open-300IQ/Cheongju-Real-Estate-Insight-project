package com.example.iq300.repository;

import com.example.iq300.domain.Board;
import org.springframework.data.domain.Page; // (추가)
import org.springframework.data.domain.Pageable; // (추가)
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // (추가) 기본 페이징 기능
    Page<Board> findAll(Pageable pageable);
    
    // (추가) 제목으로 검색
    Page<Board> findBySubjectContaining(String kw, Pageable pageable);
    
    // (추가) 내용으로 검색
    Page<Board> findByContentContaining(String kw, Pageable pageable);
    
    // (추가) 작성자 이름으로 검색 (참고용)
    // Page<Board> findByAuthor_UsernameContaining(String kw, Pageable pageable);
}