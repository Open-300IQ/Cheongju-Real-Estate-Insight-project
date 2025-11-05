package com.example.iq300.controller;

import com.example.iq300.domain.Board;
import com.example.iq300.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardService boardService;

    /**
     * (수정) 메인 페이지 ("/")
     * - 이제 '게시판' 페이지(index.html)를 직접 보여줍니다.
     */
    @GetMapping("/")
    public String root(Model model) {
        List<Board> boardList = this.boardService.getList();
        model.addAttribute("boardList", boardList);
        return "index"; // templates/index.html
    }

    /**
     * (유지) 자료 분석하기 페이지
     */
    @GetMapping("/analysis")
    public String analysisPage() {
        return "analysis"; // templates/analysis.html
    }

    /**
     * (유지) AI 상담받기 페이지
     */
    @GetMapping("/ai")
    public String aiPage() {
        return "ai"; // templates/ai.html
    }
}