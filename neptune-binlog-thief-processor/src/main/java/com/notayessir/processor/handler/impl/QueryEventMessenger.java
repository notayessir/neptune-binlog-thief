package com.notayessir.processor.handler.impl;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Queue;


@Component
public class QueryEventMessenger {

    private volatile boolean alert;

    private static final Queue<String> queue = new ArrayDeque<>();


    public void pushMessage(String database){
        alert = true;
        queue.offer(database);
    }

    public String popMessage(){
        alert = false;
        return queue.remove();
    }

    public boolean isAlert() {
        return alert;
    }

}
