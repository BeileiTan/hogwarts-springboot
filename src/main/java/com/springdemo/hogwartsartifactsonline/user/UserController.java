package com.springdemo.hogwartsartifactsonline.user;

import com.springdemo.hogwartsartifactsonline.system.Result;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import com.springdemo.hogwartsartifactsonline.user.converter.UserDtoToUserConverter;
import com.springdemo.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import com.springdemo.hogwartsartifactsonline.user.dto.UserDto;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        User foundUser = userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @GetMapping
    public Result findAllUsers(){
        List<User> foundUsers = this.userService.findAll();
        //convert foundUsers to a list of userDto
        List<UserDto> foundUsersDto = foundUsers.stream().
                map(foundUser -> this.userToUserDtoConverter.convert(foundUser))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find Success", foundUsersDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody User newUser){
        User savedUser = this.userService.save(newUser);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto){
        //the input is userDto since(the input have to ignore the password)
        //in order to use the update method, the userDto has to covert into the user
        User user = this.userDtoToUserConverter.convert(userDto);
        //then we need to use the update method
        User updatedUser = this.userService.update(userId, user);
        //we need to send the json that not include the password
        UserDto updatedUserDto = this.userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
