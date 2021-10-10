package com.calzproject.toDoList.exceptions;

import com.calzproject.toDoList.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@ControllerAdvice  // for exception advise from controller
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER=LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

//    One Method of ResponseEntityExceptionHandler => handleExcpetion
//    Parameters:
//    ex - the target exception
//    request - the current request
    @ExceptionHandler // defined class method
    public final ResponseEntity<Object> handleProjectIdException(ProjectIdException ex, WebRequest request){
//       Can not use autowire here. because this class did not have any anotation
        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>Here is CustomResponseEntityExceptionHandler");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectNotFound(ProjectNotFoundException ex, WebRequest request){
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUserException(UserException ex, WebRequest request){
        UserExceptionResponse exceptionResponse = new UserExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
