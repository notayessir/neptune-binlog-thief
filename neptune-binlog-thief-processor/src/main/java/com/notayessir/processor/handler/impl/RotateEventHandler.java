package com.notayessir.processor.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.common.AppContextProvider;
import com.notayessir.common.packet.event.RotateEvent;
import com.notayessir.processor.configure.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RotateEventHandler extends AbsPacketHandler<RotateEvent> {


    @Autowired
    AppConfiguration appConfiguration;


    @Autowired
    TableMapEventCacheManager cacheManager;

    @Autowired
    AppContextProvider appContextProvider;


    @Override
    public void beforeHandle(RotateEvent packet) {
        LOG.info(JSONObject.toJSONString(packet));
        appContextProvider.setCurrentBinlogFile(packet.getNextBinlogName());
        appContextProvider.setCurrentBinlogPos(packet.getPosition());
    }

    @Override
    public void handle(RotateEvent packet) {
        // clear table map
        cacheManager.clear();
    }

    @Override
    public boolean skipHandle(RotateEvent packet, AppConfiguration appConfiguration) {
        return false;
    }
}
