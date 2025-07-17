package com.project.backend.controllers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> entityAlreadyExists(DataIntegrityViolationException ex){
        return new ResponseEntity<>("Bad request load", HttpStatus.BAD_REQUEST);

    }

}
