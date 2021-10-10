package com.calzproject.toDoList.services;

import com.calzproject.toDoList.ToDoListApplication;
import com.calzproject.toDoList.domain.Backlog;
import com.calzproject.toDoList.domain.Project;
import com.calzproject.toDoList.domain.User;
import com.calzproject.toDoList.exceptions.ProjectIdException;
import com.calzproject.toDoList.exceptions.ProjectNotFoundException;
import com.calzproject.toDoList.repositories.BacklogRepository;
import com.calzproject.toDoList.repositories.ProjectRepository;
import com.calzproject.toDoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;



@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {
        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            //System.out.println(!existingProject.getProjectLeader().equals(username));
            if (existingProject != null && !existingProject.getUser().getUsername().equals(username)) {
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: " + project.getProjectIdentifier() + " cannot be updated because it doesn't exist");
            }
        }


        // In the service layer try catch can pass your own data to the @ExceptionHandler method,
        try {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());


            String identifier = project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(identifier);
//           for create new project item
            if (project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(identifier);
            }
//          for update the project item
            if (project.getId() != null ) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(identifier));
            }


            // @Column(updatable = false, unique = true)
            // private String projectIdentifier;
            // Unique check happened here
            return projectRepository.save(project);
        }catch (Exception e) {
            throw new ProjectIdException("Project ID " + project.getProjectIdentifier().toUpperCase()+" already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID " + projectId +" doesn't exist");
        }

        if (!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public List<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier (String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

}
