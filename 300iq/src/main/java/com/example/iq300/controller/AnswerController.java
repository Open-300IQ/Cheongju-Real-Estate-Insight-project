package com.example.iq300.controller;

import com.example.iq300.domain.Question;
import com.example.iq300.domain.User;
import com.example.iq300.service.AnswerService;
import com.example.iq300.service.QuestionService;
import com.example.iq300.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    /**
     * 답변 등록 처리 (POST)
     * @param id 답변이 달릴 '질문'의 ID
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id,
                               @Valid AnswerCreateForm answerCreateForm, BindingResult bindingResult, Principal principal) {
        
        Question question = this.questionService.getQuestion(id); // 1. 질문을 찾고
        User user = this.userService.findUser(principal.getName()) // 2. 답변자를 찾고
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (bindingResult.hasErrors()) {
            // 폼 유효성 검사 실패 시,
            model.addAttribute("question", question); // (중요) 상세 보기 페이지를 다시 렌더링해야 하므로 question 객체를 모델에 담아줌
            return "question_detail"; // question_detail.html을 다시 보여줌
        }

        // 3. 답변 생성 서비스 호출
        this.answerService.create(question, answerCreateForm.getContent(), user);
        
        // 4. 성공 시, 해당 질문 상세 페이지로 새로고침
        return String.format("redirect:/question/detail/%s", id);
    }
}