package com.example.iq300.controller;

import com.example.iq300.domain.RealEstateTerm;
import com.example.iq300.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/dictionary")
@RequiredArgsConstructor
@Controller

public class DictionaryController {

    private final DictionaryService dictionaryService;
    
    // 'ㄱ'부터 'A-Z'까지의 자음/알파벳 배열
    private final String[] CHAR_TABS = {
        "ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ", "A-Z"
    };

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value="part", defaultValue="ㄱ") String part,
                       @RequestParam(value="searchType", defaultValue="term") String searchType,
                       @RequestParam(value="kw", defaultValue="") String kw) {

        // 1. 서비스에서 검색 결과 조회
        List<RealEstateTerm> termList = dictionaryService.getList(part, searchType, kw);

        // 2. 모델에 데이터 추가
        model.addAttribute("termList", termList);
        model.addAttribute("charTabs", CHAR_TABS); // 'ㄱ','ㄴ','ㄷ'... 탭 배열
        
        // 3. 검색 조건 유지를 위해 모델에 추가
        model.addAttribute("part", part);
        model.addAttribute("searchType", searchType);
        model.addAttribute("kw", kw);
        model.addAttribute("activeMenu", "dictionary");

        return "dictionary_list"; // templates/dictionary_list.html 파일을 반환
    }
}