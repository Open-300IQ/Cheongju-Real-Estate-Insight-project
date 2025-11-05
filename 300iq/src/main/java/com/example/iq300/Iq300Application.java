package com.example.iq300;

// import com.example.iq300.domain.User; // (삭제)
// import com.example.iq300.service.BoardService; // (삭제)
import com.example.iq300.service.CsvDataService;
import com.example.iq300.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// import java.util.Optional; // (삭제)

@SpringBootApplication
@RequiredArgsConstructor
public class Iq300Application {

    private final CsvDataService csvDataService;
    private final UserService userService;
    // private final BoardService boardService; // (삭제)

    public static void main(String[] args) {
        SpringApplication.run(Iq300Application.class, args);
    }

    @Bean
    public CommandLineRunner initData() {
        return (args) -> {
            System.out.println("====== (시작) CSV 데이터 DB 적재 ======");

            // 1. CSV 데이터 로드
            csvDataService.loadAllPriceTimeSeriesData();
            csvDataService.loadAllRealEstateTransactions();
            
            System.out.println("데이터 로드 완료. 사용자 데이터 생성 시작...");

            // 2. 관리자(admin) 및 일반 사용자(user1) 생성
            // (isVerified 문제가 해결되어 이제 정상 실행되어야 합니다)
            try {
                if (userService.findUser("admin").isEmpty()) {
                    userService.create("admin", "admin@test.com", "1234");
                    System.out.println("사용자(admin) 생성 완료.");
                }
                
                if (userService.findUser("user1").isEmpty()) {
                    userService.create("user1", "user1@test.com", "1234");
                    System.out.println("사용자(user1) 생성 완료.");
                }
            } catch (Exception e) {
                System.out.println("사용자 생성 중 오류: " + e.getMessage());
            }

            // 3. (삭제) 테스트 게시글 생성 로직 (사용자님 요청대로 삭제)

            System.out.println("====== (완료) DB 데이터 적재 완료 ======");
        };
    }
}