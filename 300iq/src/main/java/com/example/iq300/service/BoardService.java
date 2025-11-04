package com.example.iq300.service;

import com.example.iq300.domain.Board;
import com.example.iq300.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; // import 문이 있는지 확인!

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * (FR-001) 모든 게시글 조회
     * ❗️이 메서드가 누락되었을 가능성이 높습니다.❗️
     */
    public List<Board> getAllPosts() {
        return boardRepository.findAll();
    }

    /**
     * (FR-002) 새 게시글 저장
     */
    public Board createPost(Board board) {
        return boardRepository.save(board);
    }

    /**
     * (FR-001) ID로 특정 게시글 조회
     */
    public Board getPostById(Long id) {
        return boardRepository.findById(id).orElse(null); 
    }

    /**
     * (FR-004) 게시글 수정
     */
    public Board updatePost(Long id, Board updatedPost) {
        Board existingPost = boardRepository.findById(id).orElse(null);

        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setAuthor(updatedPost.getAuthor());
            
            return boardRepository.save(existingPost);
        }
        return null;
    }

    /**
     * (FR-005) 게시글 삭제
     */
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}