package com.calzproject.toDoList.repositories;

import com.calzproject.toDoList.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectId);

    @Override
    List<Project> findAll();

    List<Project> findAllByProjectLeader(String username);
}
