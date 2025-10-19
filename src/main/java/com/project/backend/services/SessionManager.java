package com.project.backend.services;

import java.util.HashMap;
import java.util.Map;

import com.project.backend.data.ExamFormatRepository;
import com.project.backend.data.ExamScoreRepository;
import com.project.backend.data.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class SessionManager {

    Map<String, ExamSession> sessions;

    @Autowired
    ExamFormatRepository formatRepo;

    @Autowired
    ExamScoreRepository scoreRepo;

    @Autowired
    SourceRepository sourceRepo;
    public SessionManager(){
        sessions = new HashMap();
    }
    public String newSession(String formatId){
        String id = UUIDCreator.generateType1UUID().toString();
        sessions.put(id, new ExamSession(this, formatId));
        return id;
    }

    public String start(String sessionId){
        ExamSession sess = sessions.get(sessionId);
        sess.start();
        return "Session Started";
    }
   
}
