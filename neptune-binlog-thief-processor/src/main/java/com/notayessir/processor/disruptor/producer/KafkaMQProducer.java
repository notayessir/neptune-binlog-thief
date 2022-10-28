package com.notayessir.processor.disruptor.producer;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class KafkaMQProducer implements EventHandler<BinlogEvent> {


    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQProducer.class);



    private KafkaProducer<String, String> producer;

    @Autowired
    AppConfiguration appConfiguration;


    private boolean enable;


    @PostConstruct
    private void init(){
        if (StringUtils.isAnyBlank(appConfiguration.getKafkaNameserver(), appConfiguration.getKafkaTopicPrefix())){
            return;
        }
        try {
            Map<String, Object> props = new HashMap<>();
            props.put("bootstrap.servers", appConfiguration.getKafkaNameserver());
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 32);
            props.put("linger.ms", 3);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(props);
            enable = true;
        } catch (KafkaException e){
            LOG.info("error happened when init kafka mq client, e", e);
            e.printStackTrace();
        }

    }




    public void onEvent(BinlogEvent event, long sequence, boolean endOfBatch) throws Exception {
        long timestamp = System.currentTimeMillis();
        String topic = appConfiguration.getKafkaTopicPrefix() + event.getPacketType().name();
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, String.valueOf(timestamp), JSONObject.toJSONString(event));
        try {
            Future<RecordMetadata> sendResult =  producer.send(record);
            RecordMetadata metadata = sendResult.get();
            event.clear();
//            LOG.info("sent record(key={} value={}) meta(partition={}, offset={}) time={}",
//                    record.key(), record.value(), metadata.partition(), metadata.offset(), timestamp);
        }catch (Exception e){
            LOG.error("fail to push message to kafka, msg : {}", JSONObject.toJSONString(event), e);
            e.printStackTrace();
        }

    }

    public boolean isEnable() {
        return enable;
    }
}
