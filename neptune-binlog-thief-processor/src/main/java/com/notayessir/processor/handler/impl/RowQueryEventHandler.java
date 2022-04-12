package com.notayessir.processor.handler.impl;

import com.notayessir.common.packet.event.RowQueryEvent;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RowQueryEventHandler extends AbsPacketHandler<RowQueryEvent> {


    @Autowired
    private QueryEventMessenger queryEventMessenger;

    @Override
    public void handle(RowQueryEvent packet) {
        String database = null;
        if (queryEventMessenger.isAlert()){
            database = queryEventMessenger.popMessage();
        }
        BinlogEvent event = buildBinlogEvent(packet);
        event.setDatabase(database);
        binlogEventProducer.publishEvent(event);

    }

    @Override
    public boolean skipHandle(RowQueryEvent packet, AppConfiguration appConfiguration) {
        return false;
    }


    private BinlogEvent buildBinlogEvent(RowQueryEvent event){
        BinlogEvent binlogEvent = new BinlogEvent();
        binlogEvent.setPacketType(event.getPacketType());
        binlogEvent.setQuery(event.getQuery());
        binlogEvent.setEventHeader(event.getEventHeader());
        binlogEvent.setPacketHeader(event.getPacketHeader());
        return binlogEvent;
    }

}
