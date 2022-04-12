package com.notayessir.processor.handler.impl;


import com.notayessir.processor.configure.AppConfiguration;
import org.springframework.stereotype.Component;


@Component
public class NoopHandler extends AbsPacketHandler<Object> {


    @Override
    public void handle(Object packet) {
    }

    @Override
    public boolean skipHandle(Object packet, AppConfiguration appConfiguration) {
        return false;
    }
}
