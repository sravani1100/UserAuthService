package com.example.UserAuthService.services;

import com.example.UserAuthService.dtos.SendEmailEventDto;
import com.example.UserAuthService.exceptions.InvalidTokenException;
import com.example.UserAuthService.exceptions.UserAlreadyExistsException;
import com.example.UserAuthService.exceptions.UserNotSignedException;
import com.example.UserAuthService.models.Token;
import com.example.UserAuthService.models.User;
import com.example.UserAuthService.repos.TokenRepository;
import com.example.UserAuthService.repos.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.exceptions.PasswordExpiredException;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{


    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public AuthService(UserRepo userRepo,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       TokenRepository tokenRepository,
                       KafkaTemplate<String, String> kafkaTemplate,
                       ObjectMapper objectMapper){
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signup(String name, String email, String password, String phoneNumber) throws JsonProcessingException {
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("Please try login directly!!");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user = userRepo.save(user);

        SendEmailEventDto sendEmailEventDto = new SendEmailEventDto();
        sendEmailEventDto.setSubject("Welcome to Scaler!");
        sendEmailEventDto.setBody("Welcome aboard.");
        sendEmailEventDto.setToEmail(email);

        kafkaTemplate.send(
                "sendEmailEvent",
                objectMapper.writeValueAsString(sendEmailEventDto)
        );

        return user;
    }

    public Token login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isEmpty()){
            throw new UserNotSignedException("Please try signup first.");
        }

        if(!bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())){
            throw new PasswordExpiredException("Please type correct password.");
        }

        Token token = new Token();
        token.setUser(userOptional.get());
        token.setValue(RandomStringUtils.randomAlphabetic(128));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date dateAfter30Days = calendar.getTime();

        token.setExpiresAt(dateAfter30Days);

        return tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) {

        Optional<Token> optionalToken = tokenRepository.findByValueAndExpiresAtAfter(tokenValue, new Date());
        if(optionalToken.isEmpty()){
            //throw new InvalidTokenException("Invalid token");
            return null;
        }
        return optionalToken.get().getUser();
    }

}
