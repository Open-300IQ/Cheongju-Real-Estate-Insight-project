package com.example.iq300.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // (필수 임포트)
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "realestateterm") // <--- DB 테이블 이름과 100% 일치하도록 명시
public class RealEstateTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String term;

    @Column(columnDefinition = "TEXT")
    private String definition; 
}