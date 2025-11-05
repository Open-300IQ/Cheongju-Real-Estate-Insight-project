package com.example.iq300.repository;

import com.example.iq300.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // JpaRepository가 기본적인 CRUD(Create, Read, Update, Delete)를 모두 제공합니다.
}