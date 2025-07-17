package com.project.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.backend.services.AuthenticationService;
import com.project.backend.beans.Question;
import com.project.backend.data.QuestionsRepository;
import com.project.backend.beans.AuthenticationRequest;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController{

    @Autowired
    private QuestionsRepository repository;

    @PostMapping("/addquestions")
    public HttpStatus addQuestions(@RequestBody List<Question> questions){
        repository.saveAll(questions);
        return HttpStatus.ACCEPTED;
    }
}