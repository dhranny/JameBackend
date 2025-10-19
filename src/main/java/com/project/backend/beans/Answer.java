package com.project.backend.beans;

import lombok.Data;

@Data
public class Answer {
    String sessionId;
    int questionNUm;
    char answer;
}
