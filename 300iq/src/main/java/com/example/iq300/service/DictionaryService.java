package com.example.iq300.service;

import com.example.iq300.domain.RealEstateTerm; // (필수 임포트)
import com.example.iq300.repository.RealEstateTermRepository; // (필수 임포트)
import lombok.RequiredArgsConstructor; // (필수 임포트)
import org.springframework.stereotype.Service;
import java.util.List; // (필수 임포트)

@RequiredArgsConstructor
@Service
public class DictionaryService {

    private final RealEstateTermRepository realEstateTermRepository;

    /**
     * 용어사전 목록을 검색 조건에 맞게 조회합니다.
     * @param part 'ㄱ', 'ㄴ' 등 자음 탭
     * @param searchType "term"(용어명) 또는 "content"(용어내용)
     * @param kw 검색 키워드
     * @return 검색된 용어 리스트
     */
    public List<RealEstateTerm> getList(String part, String searchType, String kw) {
        
        // 1. 키워드가 없는 경우 (자음 탭만 클릭)
        if (kw == null || kw.isEmpty()) {
            return realEstateTermRepository.findByInitial(part); // <-- Repository 호출
        }

        // 2. 키워드가 있는 경우 (검색 타입에 따라 분기)
        if ("content".equals(searchType)) {
            // 용어내용으로 검색
            return realEstateTermRepository.findByInitialAndDefinitionContaining(part, kw);
        } else {
            // 용어명으로 검색 (기본값)
            return realEstateTermRepository.findByInitialAndTermContaining(part, kw);
        }
    }
}