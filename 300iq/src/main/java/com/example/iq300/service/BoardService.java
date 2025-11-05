package com.example.iq300.service;

import com.example.iq300.domain.Board;
import com.example.iq300.domain.User;
import com.example.iq300.exception.DataNotFoundException;
import com.example.iq300.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시판 목록 조회
    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    // 게시글 상세 조회
    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    // 게시글 생성
    public void create(String subject, String content, User author) {
        Board b = new Board();
        b.setSubject(subject);
        b.setContent(content);
        b.setCreateDate(LocalDateTime.now());
        b.setAuthor(author); // 작성자 설정
        this.boardRepository.save(b);
    }
}