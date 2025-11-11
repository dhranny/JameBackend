/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.project.backend.controllers;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.project.backend.beans.User;
import com.project.backend.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.beans.ExamFormat;
import com.project.backend.beans.ExamScore;
import com.project.backend.data.ExamFormatRepository;
import com.project.backend.data.ExamScoreRepository;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private SessionManager sessManager;

    @Autowired
    private ExamFormatRepository formatRepo;

    @Autowired 
    private ExamScoreRepository scoreRepo;

    @PostMapping("/init")
    public ResponseEntity<String> initExam(@RequestBody String formatId){
        String sessId = sessManager.newSession(formatId);
        return new ResponseEntity<>(sessId, HttpStatus.CREATED);
    }

    @PostMapping("/format")
    public ResponseEntity<String> createFormat(@RequestBody ExamFormat examFormat){
        ExamFormat nFormat = formatRepo.save(examFormat);
        return new ResponseEntity<>(nFormat.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/format")
    public ResponseEntity<List<ExamFormat>> getFormat(){
        List<ExamFormat> formats = formatRepo.findAll();
        return new ResponseEntity<>(formats, HttpStatus.CREATED);
    }

    @GetMapping("/scorehistory")
    public ResponseEntity<List<ExamScore>> getHistory(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(scoreRepo.findAll());
    }

   

}
