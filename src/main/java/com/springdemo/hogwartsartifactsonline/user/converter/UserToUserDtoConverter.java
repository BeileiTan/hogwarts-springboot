package com.springdemo.hogwartsartifactsonline.user.converter;

import com.springdemo.hogwartsartifactsonline.user.User;
import com.springdemo.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User source){
        UserDto userDto = new UserDto(
                source.getId(),
                source.getUsername(),
                source.getEnabled(),
                source.getRoles()
        );
        return userDto;
    }
}
