package com.project.backend.services;

import com.project.backend.beans.*;
import com.project.backend.data.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.backend.data.ExamFormatRepository;
import com.project.backend.data.ExamScoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamSession {


    ScheduleManager schManager;
    SessionManager sessMng;

    char[] answers;
    Question[] questions;
    ExamFormat examFormat;

    public ExamSession(SessionManager sessMng, String formatId){
        this.sessMng = sessMng;
        System.out.println(sessMng.formatRepo.count() + formatId);
        examFormat = sessMng.formatRepo.findById(formatId).orElseThrow();
        answers = new char[examFormat.getNumOfQuestions()];
        questions = loadQuestions();
        schManager = new ScheduleManager(this, examFormat.getDurationInSeconds());
    }

    private Question[] loadQuestions(){
        Source source = sessMng.sourceRepo.findById(examFormat.getSourceId()).get();
        List<Question> questions = source.getQuestions();
        return questions.toArray(new Question[0]);
    }

    public void start(){
        schManager.start();
    }

    public int generateReport(){
        int score = 0;
        for(int i = 0; i < examFormat.getNumOfQuestions(); i++){
            if(answers[i] == questions[i].getAnswer())
                score++;
        }
        ExamScore scoreObj = new ExamScore();
        scoreObj.setScore(score);
        scoreObj.setQuestions(Arrays.asList(questions));
        scoreObj.setFormatId(examFormat.getId());
        sessMng.scoreRepo.save(scoreObj);
        return score;
    }

    public String answer(Answer answer){
        answers[answer.getQuestionNUm()] = answer.getAnswer();
        return "Answer submitted successfully";
    }

}
