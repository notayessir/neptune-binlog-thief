package com.notayessir.processor.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.common.AppContextProvider;
import com.notayessir.common.packet.Packet;
import com.notayessir.processor.disruptor.BinlogEventProducer;
import com.notayessir.processor.handler.PacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class AbsPacketHandler<P> implements PacketHandler<P> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());


    @Autowired
    protected AppContextProvider appContextProvider;


    @Autowired
    protected BinlogEventProducer binlogEventProducer;

    @Override
    public void beforeHandle(P packet) {
        // log to file
        LOG.info(JSONObject.toJSONString(packet));
        // update binlog position
        Packet pc = (Packet) packet;
        if (Objects.nonNull(pc.getEventHeader())){
            appContextProvider.setCurrentBinlogPos(pc.getEventHeader().getLogPos());
        }
    }
}
