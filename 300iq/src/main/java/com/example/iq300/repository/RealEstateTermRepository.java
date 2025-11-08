package com.example.iq300.repository;

import com.example.iq300.domain.RealEstateTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // (필수 임포트)
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RealEstateTermRepository extends JpaRepository<RealEstateTerm, Long> {

    // 1. 'ㄱ', 'ㄴ' 등 자음 탭으로 검색: WHERE term LIKE 'ㄱ%'
    @Query("SELECT t FROM RealEstateTerm t WHERE t.term LIKE :part%") 
    // [수정] @Param 추가
    List<RealEstateTerm> findByInitial(@Param("part") String part);
    
    // 2. 자음 탭 + 용어명(term) 키워드로 검색
    @Query("SELECT t FROM RealEstateTerm t WHERE t.term LIKE :part% AND t.term LIKE %:kw%")
    // [수정] @Param 추가
    List<RealEstateTerm> findByInitialAndTermContaining(@Param("part") String part, @Param("kw") String kw);

    // 3. 자음 탭 + 용어내용(definition) 키워드로 검색
    @Query("SELECT t FROM RealEstateTerm t WHERE t.term LIKE :part% AND t.definition LIKE %:kw%")
    // [수정] @Param 추가
    List<RealEstateTerm> findByInitialAndDefinitionContaining(@Param("part") String part, @Param("kw") String kw);
}