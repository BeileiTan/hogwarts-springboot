package com.springdemo.hogwartsartifactsonline.user.converter;

import com.springdemo.hogwartsartifactsonline.user.User;
import com.springdemo.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter <UserDto, User>{

    @Override
    public User convert(UserDto source) {
        User user = new User();
        user.setId(source.id());
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());
        return user;
    }
}
