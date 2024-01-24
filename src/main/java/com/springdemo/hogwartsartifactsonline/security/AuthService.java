package com.springdemo.hogwartsartifactsonline.security;

import com.springdemo.hogwartsartifactsonline.user.User;
import com.springdemo.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import com.springdemo.hogwartsartifactsonline.user.dto.MyUserPrincipal;
import com.springdemo.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public final JwtProvider jwtProvider;

    public final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser(); //this included the password
        UserDto userDto = this.userToUserDtoConverter.convert(user);
        //create a jwt
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
