
package com.project.backend.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleManager {

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int timeInSeconds= -1;
    private ExamSession session;

    ScheduleManager(ExamSession session, int durationInSeconds){
        this.session = session;
        if(durationInSeconds <= 0)
            throw new IllegalStateException();
        timeInSeconds = durationInSeconds;
    }

    Runnable counter = new Runnable() {
        public void run() {
            if(timeInSeconds <= 0)
                return;
            timeInSeconds--;
        }
    };
    
   

    public void start(){
        if(timeInSeconds == -1)
            throw new IllegalStateException();
        scheduler.scheduleAtFixedRate(counter, 0, 1, TimeUnit.SECONDS);
    }

    

}
