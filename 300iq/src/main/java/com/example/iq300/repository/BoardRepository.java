package com.example.iq300.repository;

import com.example.iq300.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 기본적인 CRUD (save, findById, findAll, delete) 메서드가 자동 생성됩니다.
}