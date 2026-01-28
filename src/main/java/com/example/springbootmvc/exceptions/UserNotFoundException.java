package com.example.springbootmvc.exceptions;

public class UserNotFoundException extends EntityNotFound {

    public UserNotFoundException(String message) {
        super(message);
    }
}
