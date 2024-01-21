package com.springdemo.hogwartsartifactsonline.user;

import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp() {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setRoles("admin user");
        u1.setPassword("ABCDEfghi123456*()");
        u1.setEnabled(true);

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setRoles("user");
        u2.setPassword("ABCDEfghi123456*()");
        u2.setEnabled(true);

        User u3 = new User();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setRoles("user");
        u3.setPassword("ABCDEfghi123456*()");
        u3.setEnabled(false);

        this.users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);
        this.users.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess() {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setRoles("admin user");
        u1.setPassword("ABCDEfghi123456*()");
        u1.setEnabled(true);

        given(userRepository.findById(1)).willReturn(Optional.of(u1));

        User returnedUser = userService.findById(1);

        assertThat(returnedUser.getId()).isEqualTo(u1.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(u1.getUsername());
        assertThat(returnedUser.getRoles()).isEqualTo(u1.getRoles());
        assertThat(returnedUser.getEnabled()).isEqualTo(u1.getEnabled());
        verify(userRepository,times(1)).findById(1);
    }

    @Test
    void findIdNotFound(){
        //given
        given(userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() ->{
            User returnedWizard = userService.findById(4);
        });

        //then
        assertThat(thrown).
                isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find the user with id 4 :(");
        verify(userRepository, times(1)).findById(4);
    }

    @Test
    void testFindAllSuccess(){
        //given
        given((userRepository.findAll())).willReturn(this.users);

        //when
        List<User> actualUsers = userService.findAll();

        //then
        assertThat(actualUsers.size()).isEqualTo(this.users.size());
        verify(userRepository, times( 1)).findAll();

    }

    @Test
    void testSaveSucces(){
        //given
        User u4 = new User();
        u4.setId(4);
        u4.setUsername("lily");
        u4.setRoles("user");
        u4.setPassword("ABCDEfghi123456*()");
        u4.setEnabled(true);
        given(userRepository.save(u4)).willReturn(u4);

        //when

        User savedUser = userService.save(u4);

        //then
        assertThat(savedUser.getId()).isEqualTo(u4.getId());
        assertThat(savedUser.getRoles()).isEqualTo(savedUser.getRoles());
        assertThat(savedUser.getUsername()).isEqualTo(u4.getUsername());
        assertThat(savedUser.getEnabled()).isEqualTo(u4.getEnabled());
        verify(userRepository, times( 1)).save(u4);
    }

    @Test
    void testUpdateSuccess(){
        //given
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setRoles("admin user");
        oldUser.setPassword("ABCDEfghi123456*()");
        oldUser.setEnabled(true);

        given(userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        User update = new User();
        update.setId(1);
        update.setUsername("john-updated");
        update.setRoles("admin user");
        update.setPassword("ABCDEfghi123456*()");
        update.setEnabled(true);

        //when
         User updatedUser = userService.update(1, update);

        //then
        assertThat(updatedUser.getId()).isEqualTo(update.getId());
        assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        assertThat(updatedUser.getEnabled()).isEqualTo(update.getEnabled());
        assertThat(updatedUser.getRoles()).isEqualTo(update.getRoles());
        verify(userRepository, times( 1)).findById(1);
        verify(userRepository, times( 1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound(){
        User update = new User();
        update.setId(1);
        update.setUsername("john-updated");
        update.setRoles("admin user");
        update.setPassword("ABCDEfghi123456*()");
        update.setEnabled(true);

        //given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () ->{
            userService.update(1, update);
        });

        //then
        verify(userRepository, times( 1)).findById(1);
    }

    @Test
    void testDeleteSuccess(){
        //given -> no json sent from front-end
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setRoles("admin user");
        user.setPassword("ABCDEfghi123456*()");
        user.setEnabled(true);

        given(userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        //when
        userService.delete(1);

        //then
        verify(userRepository, times( 1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        //given
        given(userRepository.findById(5)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            userService.delete(5);
        });

        //then
        verify(userRepository, times( 1)).findById(5);
    }
}