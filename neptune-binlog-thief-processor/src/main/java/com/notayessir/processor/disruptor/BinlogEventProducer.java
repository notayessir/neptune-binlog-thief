package com.notayessir.processor.disruptor;


import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.notayessir.processor.disruptor.producer.KafkaMQProducer;
import com.notayessir.processor.disruptor.producer.LogProducer;
import com.notayessir.processor.disruptor.producer.RedisProducer;
import com.notayessir.processor.disruptor.producer.RocketMQProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * disruptor 生产者
 */
@Service
public class BinlogEventProducer {

    private RingBuffer<BinlogEvent> ringBuffer;


    @Autowired
    LogProducer logProducer;

    @Autowired
    RedisProducer redisProducer;

    @Autowired
    RocketMQProducer rocketMQProducer;

    @Autowired
    KafkaMQProducer kafkaMQProducer;



    private static final EventTranslatorOneArg<BinlogEvent, BinlogEvent> TRANSLATOR = (target, sequence, source) -> BeanUtils.copyProperties(source, target);


    public static final EventFactory<BinlogEvent> eventFactory = BinlogEvent::new;



    @PostConstruct
    public void init(){
        // bufferSize is 524288
        int bufferSize = Double.valueOf(Math.pow(2, 19)).intValue();

        Disruptor<BinlogEvent> disruptor = new Disruptor<>(eventFactory, bufferSize,
                DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new SleepingWaitStrategy());
        ringBuffer = disruptor.getRingBuffer();
        EventHandler<BinlogEvent> handler;
        // support single producer only
        if (rocketMQProducer.isEnable()){
            handler = rocketMQProducer;
        } else if (kafkaMQProducer.isEnable()){
            handler = kafkaMQProducer;
        } else if (redisProducer.isEnable()){
            handler = redisProducer;
        } else {
            // logProducer is default handler if there has any producer
            handler = logProducer;
        }
        disruptor.handleEventsWith(handler);  // sync binlog pos to database if satisfy condition
        disruptor.start();

    }

    /**
     * 发布 binlog 事件组
     * @param events    binlog 事件组
     */
    public void publishEvents(BinlogEvent [] events){
        ringBuffer.publishEvents(TRANSLATOR, events);
    }

    /**
     * 发布 binlog 事件
     * @param event     binlog 事件
     */
    public void publishEvent(BinlogEvent event){
        ringBuffer.publishEvent(TRANSLATOR, event);
    }


}
