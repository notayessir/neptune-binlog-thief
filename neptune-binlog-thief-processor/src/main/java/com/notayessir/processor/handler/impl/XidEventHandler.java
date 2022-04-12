package com.notayessir.processor.handler.impl;

import com.notayessir.common.packet.event.XidEvent;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XidEventHandler extends AbsPacketHandler<XidEvent> {


    private static final Logger LOG = LoggerFactory.getLogger(XidEventHandler.class);


    @Autowired
    private RowsEventMessenger rowsEventMessenger;

    @Override
    public void handle(XidEvent packet) {
        if (!rowsEventMessenger.isAlert()){
            return;
        }
        BinlogEvent[] binlogEvents = rowsEventMessenger.popEvent();
        for (BinlogEvent evt : binlogEvents) {
            evt.setXid(packet.getXid());
        }
        binlogEventProducer.publishEvents(binlogEvents);
//        LOG.info("{}, {}", packet.getPacketType(), JSONObject.toJSONString(packet));
    }

    @Override
    public boolean skipHandle(XidEvent packet, AppConfiguration appConfiguration) {
        return false;
    }
}
