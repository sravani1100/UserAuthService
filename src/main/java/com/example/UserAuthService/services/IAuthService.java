package com.example.UserAuthService.services;

import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    User signup(String name, String email, String password, String phoneNumber) throws JsonProcessingException;

    Token login(String email, String password);

    User validateToken(String tokenValue);
}
