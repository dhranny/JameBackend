package com.project.backend.services;

import com.project.backend.beans.Source;
import com.project.backend.data.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.backend.data.ExamFormatRepository;
import com.project.backend.data.ExamScoreRepository;
import com.project.backend.beans.ExamFormat;
import com.project.backend.beans.Question;
import com.project.backend.beans.ExamScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamSession {

    @Autowired
    ExamFormatRepository formatRepo;

    @Autowired
    ExamScoreRepository scoreRepo;

    @Autowired
    SourceRepository sourceRepo;

    ScheduleManager schManager;

    char[] answers;
    Question[] questions;
    ExamFormat examFormat;

    public ExamSession(String formatId){
        examFormat = formatRepo.findById(formatId).get();
        answers = new char[examFormat.getNumOfQuestions()];
        questions = loadQuestions();
        schManager = new ScheduleManager(this, examFormat.getDurationInSeconds());
    }

    private Question[] loadQuestions(){
        Source source = sourceRepo.findById(examFormat.getSourceId()).get();
        List<Question> questions = source.getQuestions();
        
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
        scoreRepo.save(scoreObj);
    }

}
