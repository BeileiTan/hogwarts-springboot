package com.springdemo.hogwartsartifactsonline.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.springdemo.hogwartsartifactsonline.user.dto.UserDto;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import com.springdemo.hogwartsartifactsonline.wizard.WizardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<User> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setRoles("admin user");
        u1.setPassword("123qwe");
        u1.setEnabled(true);

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setRoles("user");
        u2.setPassword("123qwe");
        u2.setEnabled(true);

        User u3 = new User();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setRoles("user");
        u3.setPassword("123qwe");
        u3.setEnabled(false);

        this.users.add(u1);
        this.users.add(u2);
        this.users.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findWizardByIdSuccess() throws Exception {
        //given
        given(userService.findById(1)).willReturn(this.users.get(0));

        //when and then
        this.mockMvc.perform(get(this.baseUrl+ "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"))
                .andExpect(jsonPath("$.data.roles").value("admin user"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    void findUserdByIdNotFound() throws Exception {
        given(userService.findById(4)).willThrow(new ObjectNotFoundException("user",4));

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl+ "/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the user with id 4 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        given(userService.findAll()).willReturn(this.users);

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl+ "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].username").value("john"))
                .andExpect(jsonPath("$.data[0].roles").value("admin user"))
                .andExpect(jsonPath("$.data[0].enabled").value(true));
    }

    @Test
    void testAddUserSuccess() throws Exception {
        User user = new User(null,true, "user", "lily", "ABCDEfghi123456*()");
        String json = this.objectMapper.writeValueAsString(user);

        //faked
        User savedUser = new User();
        savedUser.setId(4);
        savedUser.setEnabled(true);
        savedUser.setRoles("user");
        savedUser.setUsername("lily");
        savedUser.setPassword("ABCDEfghi123456*()");

        given(this.userService.save(Mockito.any(User.class))).willReturn(savedUser);


        //when and then
        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.username").value("lily"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        //Simulate what the front-end provides to the controller
        UserDto user = new UserDto(null,"new-eric", true, "user");
        String json = this.objectMapper.writeValueAsString(user);

        //fake data
        User updatedUser = new User();
        updatedUser.setId(2);
        updatedUser.setEnabled(true);
        updatedUser.setRoles("user");
        updatedUser.setUsername("new-eric");

        given(this.userService.update(eq(2), Mockito.any(User.class))).willReturn(updatedUser);

        //when and then
        this.mockMvc.perform(put(this.baseUrl + "/users/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("new-eric"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    void testUpdateUserErrorWithNonExisitedId() throws Exception {
        //Simulate what the front-end provides to the controller
        UserDto user = new UserDto(5,"new-people", true, "user");
        String json = this.objectMapper.writeValueAsString(user);

        given(this.userService.update(eq(5), Mockito.any(User.class))).willThrow(new ObjectNotFoundException("user", 5));

        //when and then
        this.mockMvc.perform(put(this.baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the user with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        //given
        doNothing().when(this.userService).delete(1);

        //When AND Then
        this.mockMvc.perform(delete(this.baseUrl+ "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserErrorWithNonExisitedId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("user", 5)).when(this.userService).delete(5);

        //When AND Then
        this.mockMvc.perform(delete(this.baseUrl+ "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the user with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}