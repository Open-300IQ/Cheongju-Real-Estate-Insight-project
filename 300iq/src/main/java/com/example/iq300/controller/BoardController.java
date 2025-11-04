package com.example.iq300.controller;

import com.example.iq300.domain.Board;
import com.example.iq300.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/board") // URL이 /board로 시작하도록 그룹화


public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 목록 페이지 (FR-001: 글 읽기)
     */
    @GetMapping("/list")
    public String listPosts(Model model) {
        List<Board> posts = boardService.getAllPosts();
        model.addAttribute("posts", posts);
        return "board/list"; // templates/board/list.html 파일을 반환
    }

    /**
     * 게시글 작성 폼 페이지 (FR-002: 글 작성)
     */
    @GetMapping("/post")
    public String postForm() {
        return "board/post_form"; // templates/board/post_form.html
    }

    /**
     * 게시글 실제 저장 (FR-002: 글 작성)
     */
    @PostMapping("/post")
    public String savePost(Board board) { // 폼에서 전송된 데이터를 Board 객체로 받음
        boardService.createPost(board);
        return "redirect:/board/list"; // 저장 후 목록 페이지로 리다이렉트
    }

    /**
     * 게시글 상세 조회 페이지 (FR-001: 글 읽기)
     */
    @GetMapping("/detail/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        Board post = boardService.getPostById(id);
        model.addAttribute("post", post);
        return "board/detail"; // templates/board/detail.html
    }
    /* 게시글 수정 폼 페이지 (FR-004)
    */
   @GetMapping("/edit/{id}")
   public String editForm(@PathVariable("id") Long id, Model model) {
       Board post = boardService.getPostById(id);
       model.addAttribute("post", post);
       return "board/edit_form"; // templates/board/edit_form.html
   }

   /**
    * 게시글 실제 수정 (FR-004)
    */
   @PostMapping("/edit/{id}")
   public String updatePost(@PathVariable("id") Long id, Board board) {
       boardService.updatePost(id, board);
       return "redirect:/board/detail/" + id; // 수정 후 상세 페이지로 리다이렉트
   }

   /**
    * 게시글 삭제 (FR-005)
    */
   @GetMapping("/delete/{id}")
   public String deletePost(@PathVariable("id") Long id) {
       boardService.deletePost(id);
       return "redirect:/board/list"; // 삭제 후 목록 페이지로 리다이렉트
   }
    
}