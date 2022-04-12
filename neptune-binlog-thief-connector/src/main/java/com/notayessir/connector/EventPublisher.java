package com.notayessir.connector;

import com.notayessir.common.packet.BinlogPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * binlog 事件生产者
 */
@Component
public class EventPublisher implements ApplicationEventPublisherAware {


    private ApplicationEventPublisher publisher;


    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    /**
     * 发布/生产 binlog 事件/帧
     * @param binlogPacket    binlog 事件/帧
     */
    public void publish(BinlogPacket binlogPacket){
        publisher.publishEvent(binlogPacket);
    }

}
