package com.calzproject.toDoList.services;

import com.calzproject.toDoList.domain.Backlog;
import com.calzproject.toDoList.domain.Project;
import com.calzproject.toDoList.domain.ProjectTask;
import com.calzproject.toDoList.exceptions.ProjectIdException;
import com.calzproject.toDoList.exceptions.ProjectNotFoundException;
import com.calzproject.toDoList.repositories.BacklogRepository;
import com.calzproject.toDoList.repositories.ProjectRepository;
import com.calzproject.toDoList.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){


        //Exceptions: Project not found
//          Use following or use try catch
//        if (backlogRepository.findByProjectIdentifier(projectIdentifier) == null) {
//            throw new ProjectNotFoundException("ProjectNotFound: Project not found");
//        }

            //ProjectTasks to be added to a specific project, project!=null, backlog exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog(); //backlogRepository.findByProjectIdentifier(projectIdentifier);
            //set the backlog to projectTask
            projectTask.setBacklog(backlog);
            // IDPRO-1 (projectIdentifier - ID of the task within the project, not from Database)
            // we want our project sequence to be like this: IDPRO-1 IDPRO-2
            Integer BacklogSequence = backlog.getPTsequence();//  initial is 0
            // update the backlog sequence
            BacklogSequence++;
            backlog.setPTsequence(BacklogSequence);

            //Add Sequence to Project Task
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //Initial priority when priority null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3); // set priority low
            }

            //Initial status when status is null
            if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

    }

    public List<ProjectTask> findBacklogById(String projectIdentifier, String username) {

       projectService.findProjectByIdentifier(projectIdentifier, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }

    public ProjectTask findPTByProjectSequence(String projectIdentifier, String projectSequence, String username){
        // make sure we are searching on the existing backlog
        projectService.findProjectByIdentifier(projectIdentifier, username);
        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task: "+projectSequence+ " does not exist");
        }

        // make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(projectIdentifier)) {
            throw new ProjectNotFoundException("Project Task: "+projectSequence+ " does not exist in project: "+projectIdentifier);
        }

        return projectTask;
    }
    public ProjectTask updateByProjectSequence(ProjectTask updateTask, String projectIdentifier, String projectSequence, String username) {
        // update project task
        // find existing project task
        ProjectTask projectTask = findPTByProjectSequence(projectIdentifier, projectSequence, username);
        // replace it with updated task
        projectTask = updateTask;
        // save update
        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, String username) {
        ProjectTask projectTask = findPTByProjectSequence(projectIdentifier, projectSequence, username);
        projectTaskRepository.delete(projectTask);
    }

}
