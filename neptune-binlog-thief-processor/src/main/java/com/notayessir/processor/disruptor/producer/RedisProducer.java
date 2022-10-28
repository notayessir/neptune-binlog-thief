package com.notayessir.processor.disruptor.producer;


import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.disruptor.BinlogEvent;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class RedisProducer implements EventHandler<BinlogEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisProducer.class);


    private RedisAsyncCommands<String, String> commander;


    @Autowired
    AppConfiguration appConfiguration;

    private boolean enable;


    @PostConstruct
    public void init(){
        if (StringUtils.isBlank(appConfiguration.getRedisHost()) || Objects.isNull(appConfiguration.getRedisPort())){
            return;
        }
        RedisClient redisClient = RedisClient.create(RedisURI.create(appConfiguration.getRedisHost(), appConfiguration.getRedisPort()));
        try {
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            commander = connection.async();
            enable = true;
        }catch (RedisConnectionException e){
            LOG.info("error happened when init redis client, e", e);
            e.printStackTrace();
        }


    }


    public void onEvent(BinlogEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            RedisFuture<Long> future = commander.lpush(event.getPacketType().name(), JSONObject.toJSONString(event));
            Long result = future.get();
            event.clear();
        }catch (Exception e){
            LOG.error("fail to push message to redis, msg : {}", JSONObject.toJSONString(event), e);
            e.printStackTrace();
        }


    }

    public boolean isEnable() {
        return enable;
    }
}
