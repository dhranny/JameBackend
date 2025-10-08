package com.project.backend.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    Map<String, ExamSession> sessions;

    public SessionManager(){
        sessions = new HashMap();
    }
    public String newSession(String formatId){
        String id = UUIDCreator.generateType1UUID().toString();
        sessions.put(id, new ExamSession(formatId));
        return id;
    }

    public String start(String sessionId){
        ExamSession sess = sessions.get(sessionId);
        sess.start();
    }
   
}
