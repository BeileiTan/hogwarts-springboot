package com.springdemo.hogwartsartifactsonline.user;

import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Integer userId){
        return this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public User save(User newUser){
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
}
