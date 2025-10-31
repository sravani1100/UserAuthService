package com.example.UserAuthService.services;

import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    User signup(String name, String email, String password, String phoneNumber);

    Token login(String email, String password);

    User validateToken(String tokenValue);
}
