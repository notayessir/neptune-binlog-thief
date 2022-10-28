package com.notayessir.processor.disruptor.producer;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class RocketMQProducer implements EventHandler<BinlogEvent> {


    private static final Logger LOG = LoggerFactory.getLogger(RocketMQProducer.class);


    @Autowired
    AppConfiguration appConfiguration;

    private boolean enable;

    private DefaultMQProducer producer;

    private static final String PRODUCE_GROUP = "NEPTUNE_THIEF_PRODUCER_GROUP";


    @PostConstruct
    private void init(){
        if (StringUtils.isAnyBlank(appConfiguration.getRocketmqTopic(), appConfiguration.getRocketmqNameserver())){
            return;
        }
        producer = new DefaultMQProducer(PRODUCE_GROUP);
        producer.setNamesrvAddr(appConfiguration.getRocketmqNameserver());
        try {
            producer.start();
            enable = true;
        } catch (MQClientException e) {
            LOG.info("error happened when init rocket mq client, e", e);
            e.printStackTrace();
        }

    }

//    private final List<Message> messages = new ArrayList<>(256);

    public void onEvent(BinlogEvent event, long sequence, boolean endOfBatch) throws Exception {
//        if (!endOfBatch){
//            messages.add(buildMessage(event));
//            return;
//        } else {
//            messages.add(buildMessage(event));
//        }
        try {
            Message message = buildMessage(event);
            producer.send(message);
            event.clear();
        } catch (Exception e){
            LOG.error("fail to push message to rocket mq, msg : {}", JSONObject.toJSONString(event), e);
            e.printStackTrace();
        }

    }

    private Message buildMessage(BinlogEvent event) {
        return new Message(appConfiguration.getRocketmqTopic(), event.getPacketType().name(), JSONObject.toJSONBytes(event));
    }

    public boolean isEnable() {
        return enable;
    }

}
