package com.project.backend.controllers;

import com.project.backend.beans.Answer;
import com.project.backend.services.ExamSession;
import com.project.backend.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class StompController {

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  private ExamSession examSess;

  private SessionManager sessManager;

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public void greeting(String message) throws Exception {
    Thread.sleep(1000); // simulated delay
    System.out.println("stomp controller");
    String destination = "/topic/greetings";
    messagingTemplate.convertAndSend(destination, message);
  }

  @MessageMapping("/answer")
  public void answer(@Payload Answer answer){
    String message = examSess.answer(answer);
    String destination = "/topic/report";
    messagingTemplate.convertAndSend(destination, message);
    }

  @MessageMapping("/start")
  public void start(String sessId){
    String message = sessManager.start(sessId);
    String destination = "/topic/report";
    messagingTemplate.convertAndSend(destination, message);
    }

}
