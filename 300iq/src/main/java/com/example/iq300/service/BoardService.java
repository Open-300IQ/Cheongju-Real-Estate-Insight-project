package com.example.iq300.service;

import com.example.iq300.domain.Board;
import com.example.iq300.domain.User;
import com.example.iq300.exception.DataNotFoundException;
import com.example.iq300.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // (추가)
import org.springframework.data.domain.PageRequest; // (추가)
import org.springframework.data.domain.Pageable; // (추가)
import org.springframework.data.domain.Sort; // (추가)
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList; // (추가)
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * (수정) 게시판 목록 조회 (페이징, 검색, 정렬 기능 추가)
     */
    public Page<Board> getPage(int page, String kw, String searchType, String sortType) {
        
        // 1. 정렬(Sort) 기준 설정
        List<Sort.Order> sorts = new ArrayList<>();
        if ("popular".equals(sortType)) {
            // (참고) '인기순'은 '추천수(voter)' 기능이 없으므로 일단 '최신순'으로 대체합니다.
            // (추후 추천 기능 구현 시 'voter.size()' 등으로 변경 필요)
            sorts.add(Sort.Order.desc("createDate")); // 임시
        } else {
            // 'latest' (최신순)
            sorts.add(Sort.Order.desc("createDate"));
        }

        // 2. 페이징 설정 (페이지당 10개)
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 3. 검색(kw) 및 조회
        if (kw == null || kw.trim().isEmpty()) {
            // 검색어가 없을 때
            return this.boardRepository.findAll(pageable);
        } else {
            // 검색어가 있을 때
            if ("subject".equals(searchType)) {
                // 제목으로 검색
                return this.boardRepository.findBySubjectContaining(kw, pageable);
            } else if ("content".equals(searchType)) {
                // 내용으로 검색
                return this.boardRepository.findByContentContaining(kw, pageable);
            } else {
                // (기본값) 제목으로 검색
                return this.boardRepository.findBySubjectContaining(kw, pageable);
            }
        }
    }
    
    // (유지) 게시글 상세 조회
    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    // (유지) 게시글 생성
    public void create(String subject, String content, User author) {
        Board b = new Board();
        b.setSubject(subject);
        b.setContent(content);
        b.setCreateDate(LocalDateTime.now());
        b.setAuthor(author); 
        this.boardRepository.save(b);
    }
}