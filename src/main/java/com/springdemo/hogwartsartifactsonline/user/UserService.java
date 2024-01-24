package com.springdemo.hogwartsartifactsonline.user;

import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.springdemo.hogwartsartifactsonline.user.dto.MyUserPrincipal;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Integer userId){
        return this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public User save(User newUser){
        //we need to save encode the plain text before saving the DBf
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public User update(Integer userId, User update){
        return this.userRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setUsername(update.getUsername());
                    oldUser.setEnabled(update.getEnabled());
                    oldUser.setRoles(update.getRoles());
                    //save
                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public void delete(Integer userId){
        this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)//first find this use from database
                .map(user -> new MyUserPrincipal(user)) //if found, wrap the returned user instance in a MyUserPrincipal instance
                .orElseThrow(() -> new UsernameNotFoundException("username" + username + " is not found")); //otherwise throw out exception
    }
}
