package com.notayessir.processor.handler.impl;

import com.notayessir.common.packet.event.AnonymousGTIDEvent;
import com.notayessir.processor.configure.AppConfiguration;
import org.springframework.stereotype.Component;

@Component
public class AnonymousGTIDEventHandler extends AbsPacketHandler<AnonymousGTIDEvent> {




    @Override
    public void handle(AnonymousGTIDEvent packet) {
    }

    @Override
    public boolean skipHandle(AnonymousGTIDEvent packet, AppConfiguration appConfiguration) {
        return false;
    }



}
