package com.calzproject.toDoList.exceptions;

public class UserExceptionResponse {
    String userException;

    public UserExceptionResponse(String userException) {
        this.userException = userException;
    }

    public String getUserException() {
        return userException;
    }

    public void setUserException(String userException) {
        this.userException = userException;
    }
}
