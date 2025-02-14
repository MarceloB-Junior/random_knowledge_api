package com.api.random_knowledge.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(String message){
        super(message);
    }
}
