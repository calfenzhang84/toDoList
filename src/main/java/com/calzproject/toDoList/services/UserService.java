package com.calzproject.toDoList.services;

import com.calzproject.toDoList.domain.User;
import com.calzproject.toDoList.exceptions.UserException;
import com.calzproject.toDoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired//register in ToDoListApplication
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser) {

        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique

            // we don't persist or show the confirmPassward
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }catch (Exception err) {
            throw new UserException(newUser.getUsername() + " is existed, please change username");
        }


    }
}
