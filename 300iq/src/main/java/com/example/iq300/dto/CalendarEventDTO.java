package com.example.iq300.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 생성자를 자동으로 만들어줍니다.
public class CalendarEventDTO {
    
    private String title;     // 이벤트 제목 (예: "청주테크크노폴리스 하트리움")
    private String date;      // 날짜 (형식: "YYYY-MM-DD")
    private String rank;      // 순위 (필터링용) (예: "1순위", "2순위")
    private String className; // CSS 적용용 클래스 (색상 구분용) (예: "rank-1")
    
    // ======== [ 이 필드를 추가합니다 ] ========
    private String url;       // 클릭 시 이동할 PDF 경로 (예: "/pdf/1_2.pdf")
}