package com.example.UserAuthService.controllers;

import com.example.UserAuthService.dtos.LoginRequestDto;
import com.example.UserAuthService.dtos.SignupRequestDto;
import com.example.UserAuthService.dtos.UserDto;
import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.models.User;
import com.example.UserAuthService.services.IAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private IAuthService authService;

    public AuthController(IAuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) throws JsonProcessingException {
        User user = authService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(),signupRequestDto.getPassword(), signupRequestDto.getPhoneNumber());
        return from(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto loginRequestDto){
        Token token = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return  new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable String tokenValue){
        User user = authService.validateToken(tokenValue);

        System.out.println("Validating token........");

        return from(user);
    }

    private UserDto from(User user){

        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        return userDto;
    }


}
