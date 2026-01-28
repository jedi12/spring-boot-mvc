package com.example.springbootmvc.exceptions;

public class PetNotFoundException extends EntityNotFound {

    public PetNotFoundException(String message) {
        super(message);
    }
}
