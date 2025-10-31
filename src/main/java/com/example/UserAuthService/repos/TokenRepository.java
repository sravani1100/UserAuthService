package com.example.UserAuthService.repos;

import com.example.UserAuthService.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Override
    Token save(Token token);

    Optional<Token> findByValueAndExpiresAtAfter(String tokenValue, Date currentDate);
}
