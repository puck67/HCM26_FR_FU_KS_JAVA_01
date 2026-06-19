package com.example.lms.repository;

import com.example.lms.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t INNER JOIN t.user u WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokensByUser(Long id);

    Optional<Token> findByToken(String token);
}
