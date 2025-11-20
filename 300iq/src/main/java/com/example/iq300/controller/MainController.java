package com.example.iq300.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// (추가) Page 임포트
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.iq300.domain.Board;
import com.example.iq300.domain.MapData;
import com.example.iq300.domain.MonthlyAvgPrice;
import com.example.iq300.service.BoardService;
import com.example.iq300.service.MonthlyAvgPriceService;
import com.example.iq300.service.MapService; // (추가) MapService 임포트

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	@Value("${map.api}")
    private String mapApiKey;
	
	@Autowired
	private MonthlyAvgPriceService monthlyAvgPriceService;
	
    private final BoardService boardService;
    private final MapService mapService; // (추가) MapService 주입

    /**
     * 메인 페이지 ("/") - 자유게시판
     */
    @GetMapping("/")
    public String root(Model model,
       @RequestParam(value="page", defaultValue="0") int page,
       @RequestParam(value="kw", defaultValue="") String kw,
       @RequestParam(value="searchType", defaultValue="subject") String searchType,
       @RequestParam(value="sort", defaultValue="latest") String sortType) {
        
        Page<Board> paging = this.boardService.getPage(page, kw, searchType, sortType);
        
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);
        model.addAttribute("sortType", sortType);
        
        return "index"; // templates/index.html
    }


    @GetMapping("/analysis")
    public String analysis(Model model) {
	    	List<MonthlyAvgPrice> avgPriceList = monthlyAvgPriceService.getDistrictAvgPriceData(); 
        model.addAttribute("avgPriceData", avgPriceList);
        model.addAttribute("activeMenu", "analysis");
        return "analysis";
    }
    
    @GetMapping("/map")
    public String map(Model model) {
    	model.addAttribute("activeMenu", "map");
        model.addAttribute("mapApiKey", mapApiKey); 
        
        Map<String, List<String>> districtsAndNeighborhoods = mapService.getUniqueDistrictsAndNeighborhoods();
        List<String> guList = districtsAndNeighborhoods.keySet().stream().sorted().collect(Collectors.toList());
        model.addAttribute("guList", guList);
        model.addAttribute("districtMap", districtsAndNeighborhoods);
        
        List<MapData> allMapData = mapService.getAllMapData();
        model.addAttribute("allMapData", allMapData); 
        
        return "map";
    }
    
    /**
     * AI 상담받기 페이지
     */
//    @GetMapping("/ai")
//    public String aiPage() {
//        return "ai"; // templates/ai.html
//    }  GeminiController로 매핑 이동   

}