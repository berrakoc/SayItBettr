package com.sib.sayitbettr.Service;

import com.sib.sayitbettr.Model.User;
import com.sib.sayitbettr.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> authenticateAndGetUser(String mail, String password){
        return userRepository.findByMailAndPassword(mail, password);
    }
}
