package com.example.iq300.controller;

import com.example.iq300.domain.Board;
import com.example.iq300.domain.User;
import com.example.iq300.service.BoardService;
import com.example.iq300.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal; // (중요)

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    /**
     * 게시글 상세보기
     */
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/detail"; // templates/board/detail.html
    }

    /**
     * 게시글 생성 폼 보여주기 (GET)
     * (로그인한 사용자만 접근 가능)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String boardCreate(BoardPostForm boardPostForm) {
        return "board/post_form"; // templates/board/post_form.html
    }

    /**
     * 게시글 생성 처리 (POST)
     * (로그인한 사용자만 접근 가능)
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String boardCreate(@Valid BoardPostForm boardPostForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "board/post_form"; // 오류 시 다시 폼으로
        }
        
        // 현재 로그인한 사용자 정보 가져오기
        User user = this.userService.findUser(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        
        // 서비스로 게시글 생성
        this.boardService.create(boardPostForm.getSubject(), boardPostForm.getContent(), user);
        
        return "redirect:/"; // 메인 페이지(게시판 목록)로 이동
    }
}