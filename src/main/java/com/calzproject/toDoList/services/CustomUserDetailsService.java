package com.calzproject.toDoList.services;

import com.calzproject.toDoList.ToDoListApplication;
import com.calzproject.toDoList.domain.User;
import com.calzproject.toDoList.exceptions.UserException;
import com.calzproject.toDoList.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService { // user implements userdetail
    private static final Logger LOGGER= LoggerFactory.getLogger(ToDoListApplication.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) { //throws UsernameNotFoundException
        LOGGER.info(">>>>>>>>>>>CustomUserDetailsService");
        // for login
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOGGER.info(">>>>>>>>>>>null user");
            throw new UsernameNotFoundException(">>>>>>>>>>>>>>>>>User not found");
        }
        LOGGER.info(">>>>>>>>>>>NOT null user");
        return user;
    }
    @Transactional
    public User loadUserById(Long id) {
        LOGGER.info(">>>>>>>>>>>CustomUserDetailsServiceID");
        User user = userRepository.getById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

}

