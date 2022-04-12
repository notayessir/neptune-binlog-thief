package com.notayessir.processor.disruptor.producer;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogProducer implements EventHandler<BinlogEvent>{


    private static final Logger LOG = LoggerFactory.getLogger(LogProducer.class);


    public void onEvent(BinlogEvent event, long sequence, boolean endOfBatch) throws Exception {
        LOG.info(JSONObject.toJSONString(event));
    }

}
