package com.calzproject.toDoList.repositories;

import com.calzproject.toDoList.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <User, Long> {
    User findByUsername(String username);
    User getById(Long id);
}
