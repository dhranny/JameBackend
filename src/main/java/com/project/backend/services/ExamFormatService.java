package com.project.backend.services;

import com.project.backend.beans.ExamFormat;
import com.project.backend.beans.Question;
import com.project.backend.beans.Source;
import com.project.backend.data.ExamFormatRepository;
import com.project.backend.data.SourceRepository;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ExamFormatService {

    @Autowired
    SourceRepository sourceRepo;
    @Autowired
    ExamFormatRepository formatRepo;

    public ExamFormat getFormat(String id){
        return formatRepo.findById(id).orElseThrow();
    }

    public List<Question> loadQuestions(ExamFormat examFormat){
        Source source = sourceRepo.findById(examFormat.getSourceId()).get();
        int formatNumOfQuestion = examFormat.getNumOfQuestions();
        List<Question> questions = source.getQuestions();
        if(formatNumOfQuestion >= questions.size()){
            Collections.shuffle(questions);
            return questions.subList(0, formatNumOfQuestion);
        }
        else {
            throw new IllegalStateException("The questions are not enough for the format");
        }
    }

    public String saveFormat(ExamFormat examFormat){
        Source source = sourceRepo.findById(examFormat.getSourceId()).get();
        int formatNumOfQuestion = examFormat.getNumOfQuestions();
        List<Question> questions = source.getQuestions();
        if(formatNumOfQuestion >= questions.size()){
            ExamFormat nFormat = formatRepo.save(examFormat);
            return nFormat.getId();
        }
        else {
            throw new IllegalStateException("The questions are not enough for the format");
        }
    }

    public List<Question> loadQuestion(String id){
        ExamFormat examFormat = getFormat(id);
        return loadQuestions(examFormat);
    }

    public List<ExamFormat> getAll() {
        return formatRepo.findAll();
    }
}
