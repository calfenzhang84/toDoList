package com.calzproject.toDoList.exceptions;


import com.calzproject.toDoList.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException{
    private static final Logger LOGGER=LoggerFactory.getLogger(ProjectService.class);
//    Constructs a new runtime exception with the specified detail message
//    message - the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
    public ProjectIdException(String message) {
        super(message);
        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>Here is ProjectIdException");
    }
}
