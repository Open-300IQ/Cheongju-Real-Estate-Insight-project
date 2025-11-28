package com.example.iq300;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.iq300.service.BoardService; 
import com.example.iq300.service.CsvDataService;
import com.example.iq300.service.MonthlyAvgPriceService;
import com.example.iq300.service.QuestionService; 
import com.example.iq300.service.UserService;

import lombok.RequiredArgsConstructor;



@SpringBootApplication
@RequiredArgsConstructor
public class Iq300Application {

    private final CsvDataService csvDataService;
    private final UserService userService;
    private final BoardService boardService; 
    private final QuestionService questionService; 
    private final MonthlyAvgPriceService monthlyAvgPriceService;
    
    public static void main(String[] args) {
        SpringApplication.run(Iq300Application.class, args);
    }

  

   
    @Bean
    public CommandLineRunner initCsvData(CsvDataService csvDataService) {
        return args -> {
            System.out.println("====== [CsvDataService] 데이터 로드 시작 ======");

//            csvDataService.loadTransactions();
//            monthlyAvgPriceService.aggregateAndSaveData(); 
//            csvDataService.loadAgents();
//            csvDataService.loadPopulation();
//            csvDataService.loadTotal();
//            csvDataService.buildAndSaveFinalData();
//            
//            
//
//            csvDataService.loadRealEstateTerms(); 
//            csvDataService.loadMapTransactions();
//            csvDataService.loadHousingPolicies();
            
            System.out.println("====== [CsvDataService] 모든 데이터 로드 완료 ======");
        };
    }
    
}