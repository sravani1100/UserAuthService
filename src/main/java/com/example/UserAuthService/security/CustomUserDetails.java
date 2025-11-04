package com.example.UserAuthService.security;

import com.example.UserAuthService.models.Role;
import com.example.UserAuthService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {

    private String password;
    private String userName;
    private List<GrantedAuthority> grantedAuthorities;


    public CustomUserDetails(User user){
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.grantedAuthorities = new ArrayList<>();

        for(Role role : user.getRoles()){
            grantedAuthorities.add(new CustomGrantedAuthority(role));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
