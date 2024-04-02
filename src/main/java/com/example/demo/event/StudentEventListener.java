package com.example.demo.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentEventListener {
    @EventListener
    public void listen(StudentEvent studentEvent){
        System.out.println("listener: " + studentEvent.getTextEvent());
    }
}
