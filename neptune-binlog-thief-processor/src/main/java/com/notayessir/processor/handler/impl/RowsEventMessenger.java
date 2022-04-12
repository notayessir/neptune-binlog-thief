package com.notayessir.processor.handler.impl;

import com.notayessir.processor.disruptor.BinlogEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Queue;

@Component
public class RowsEventMessenger {


    private volatile boolean alert;

    private static final Queue<BinlogEvent[]> queue = new ArrayDeque<>(8);

    public void pushEvent(BinlogEvent[] binlogEvents){
        alert = true;
        queue.offer(binlogEvents);
    }

    public boolean isAlert() {
        return alert;
    }

    public BinlogEvent[] popEvent(){
        alert = false;
        return queue.remove();
    }


}
