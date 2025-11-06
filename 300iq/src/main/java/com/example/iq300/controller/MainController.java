package com.example.iq300.controller;

import com.example.iq300.domain.Board;
// (추가) Page 임포트
import org.springframework.data.domain.Page;
import com.example.iq300.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // (추가)

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardService boardService;

    /**
     * (수정) 메인 페이지 ("/")
     * - 페이징, 검색, 정렬 파라미터를 받아서 처리
     */
    @GetMapping("/")
    public String root(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="kw", defaultValue="") String kw,
                       @RequestParam(value="searchType", defaultValue="subject") String searchType,
                       @RequestParam(value="sort", defaultValue="latest") String sortType) {
        
        // 서비스에서 Page 객체를 받아옴
        Page<Board> paging = this.boardService.getPage(page, kw, searchType, sortType);
        
        // Model에 Page 객체와 검색어/정렬방식을 전달
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);
        model.addAttribute("sortType", sortType);
        
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