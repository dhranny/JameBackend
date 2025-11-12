package com.project.backend.controllers;

import com.project.backend.beans.Source;
import com.project.backend.data.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.project.backend.services.AuthenticationService;
import com.project.backend.beans.Question;
import com.project.backend.data.QuestionsRepository;
import com.project.backend.beans.AuthenticationRequest;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController{

    @Autowired
    private SourceRepository repository;

    @PostMapping("/addquestions")
    public ResponseEntity<String> addQuestions(@RequestBody Source source){
        source = repository.save(source);
        return new ResponseEntity<>(source.getId(), HttpStatus.ACCEPTED);
    }


}