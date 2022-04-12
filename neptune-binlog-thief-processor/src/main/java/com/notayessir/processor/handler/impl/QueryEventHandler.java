package com.notayessir.processor.handler.impl;

import com.notayessir.common.column.KeyWord;
import com.notayessir.common.packet.event.QueryEvent;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.database.manager.ColumnManager;
import com.notayessir.processor.disruptor.BinlogEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class QueryEventHandler extends AbsPacketHandler<QueryEvent> {



    @Autowired
    private ColumnManager columnManager;


    @Autowired
    private QueryEventMessenger queryEventMessenger;

    @Autowired
    private RowsEventMessenger rowsEventMessenger;

    /**
     * database and table
     * for example : ALTER TABLE `aDatabase`.`aTable` \nADD COLUMN `c1` varchar(255) NULL AFTER `c2`,
     * regex will match aDatabase and aTable
     */
    private final static Pattern PATTERN = Pattern.compile(".*?`(.*?)`\\.`(.*?)`.*");

    @Override
    public void handle(QueryEvent packet) {
        if (StringUtils.equalsIgnoreCase(KeyWord.BEGIN.getVal(), packet.getQuery())){
            // packet include database's name under this condition
            queryEventMessenger.pushMessage(packet.getSchema());
            return;
        }

        // if table's engine is myisam, summit row event to producer
        if (StringUtils.equalsIgnoreCase(KeyWord.COMMIT.getVal(), packet.getQuery())){
            BinlogEvent[] binlogEvents = rowsEventMessenger.popEvent();
            binlogEventProducer.publishEvents(binlogEvents);
            return;
        }

        if (StringUtils.containsAnyIgnoreCase(packet.getQuery(), KeyWord.ADD_COLUMN.getVal(), KeyWord.DROP_COLUMN.getVal())){
            Matcher matcher = PATTERN.matcher(packet.getQuery());
            if (matcher.find()){
                columnManager.clear(matcher.group(1), matcher.group(2));
            }
        }

        BinlogEvent event = buildBinlogEvent(packet);
        binlogEventProducer.publishEvent(event);
    }

    @Override
    public boolean skipHandle(QueryEvent packet, AppConfiguration appConfiguration) {
        return false;
    }



    private BinlogEvent buildBinlogEvent(QueryEvent event){
        BinlogEvent binlogEvent = new BinlogEvent();
        binlogEvent.setPacketType(event.getPacketType());
        binlogEvent.setQuery(event.getQuery());
        binlogEvent.setDatabase(event.getSchema());
        binlogEvent.setEventHeader(event.getEventHeader());
        binlogEvent.setPacketHeader(event.getPacketHeader());
        return binlogEvent;
    }
}
