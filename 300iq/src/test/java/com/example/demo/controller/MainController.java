package com.example.demo.controller; // 또는 본인의 메인 컨트롤러 패키지

// ▼▼▼▼▼ 1. 필요한 클래스 임포트 ▼▼▼▼▼
import com.example.iq300.domain.Board;
import com.example.iq300.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Model 임포트
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List; // List 임포트
// ▲▲▲▲▲ 1. 필요한 클래스 임포트 ▲▲▲▲▲


@Controller
@RequiredArgsConstructor // 👈 2. @RequiredArgsConstructor 추가
public class MainController {

    private final BoardService boardService; // 👈 3. BoardService 주입

    @GetMapping("/")
    public String mainPage(Model model) { // 👈 4. Model model 추가

        // 5. 게시판 목록 데이터 가져오기
        List<Board> posts = boardService.getAllPosts(); 
        
        // 6. Model에 'posts'라는 이름으로 담기
        model.addAttribute("posts", posts);

        return "index"; // templates/index.html
    }
}