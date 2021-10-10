package com.calzproject.toDoList.web;

import com.calzproject.toDoList.domain.Project;
import com.calzproject.toDoList.domain.ProjectTask;
import com.calzproject.toDoList.exceptions.ProjectNotFoundException;
import com.calzproject.toDoList.repositories.ProjectRepository;
import com.calzproject.toDoList.services.MapValidationErrorService;
import com.calzproject.toDoList.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/{backlog_projectIdentifier}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_projectIdentifier, Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_projectIdentifier, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_projectIdentifier}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlog_projectIdentifier, Principal principal) {

        return new ResponseEntity<List<ProjectTask>>(projectTaskService.findBacklogById(backlog_projectIdentifier, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{backlog_projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_projectIdentifier, @PathVariable String projectSequence, Principal principal) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_projectIdentifier, projectSequence, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_projectIdentifier,
                                               @PathVariable String projectSequence, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlog_projectIdentifier, projectSequence, principal.getName());
        return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> deleteProjectTask (@PathVariable String backlog_projectIdentifier, @PathVariable String projectSequence, Principal principal) {
        projectTaskService.deleteProjectTaskByProjectSequence(backlog_projectIdentifier, projectSequence, principal.getName());
        return new ResponseEntity<String>("ProjectTask with projectSequence " + projectSequence + " was deleted", HttpStatus.OK);
    }
}
