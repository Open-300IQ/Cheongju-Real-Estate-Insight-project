package com.example.iq300.controller;

import com.example.iq300.domain.Question;
import com.example.iq300.domain.User;
import com.example.iq300.service.QuestionService;
import com.example.iq300.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    /**
     * Q&A 게시판 목록 (자유게시판과 동일한 로직)
     */
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="kw", defaultValue="") String kw,
                       @RequestParam(value="searchType", defaultValue="subject") String searchType,
                       @RequestParam(value="sort", defaultValue="latest") String sortType) {
        
        Page<Question> paging = this.questionService.getPage(page, kw, searchType, sortType);
        
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);
        model.addAttribute("sortType", sortType);
        
        return "question_list"; // (7-5단계에서 만들 HTML)
    }

    /**
     * Q&A 상세 보기
     * (AnswerCreateForm을 모델에 추가해서 답변 폼을 미리 준비)
     */
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, AnswerCreateForm answerCreateForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail"; // (7-5단계에서 만들 HTML)
    }

    /**
     * Q&A 질문 등록 폼 (GET)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionCreateForm questionCreateForm) {
        return "question_form"; // (7-5단계에서 만들 HTML)
    }

    /**
     * Q&A 질문 등록 처리 (POST)
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionCreateForm questionCreateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        
        User user = this.userService.findUser(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        
        this.questionService.create(questionCreateForm.getSubject(), questionCreateForm.getContent(), user);
        
        return "redirect:/question/list"; // Q&A 목록으로 이동
    }
}