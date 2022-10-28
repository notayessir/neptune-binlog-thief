package com.notayessir.processor;


import com.notayessir.common.AppContextProvider;
import com.notayessir.common.BinlogPosition;
import com.notayessir.common.packet.BinlogPacket;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketType;
import com.notayessir.ha.BinlogPosClient;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.handler.PacketHandler;
import com.notayessir.processor.handler.impl.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 *  binlog 事件消费者
 */
@Component
public class BinlogEventNotifier {


    private static final Logger LOG = LoggerFactory.getLogger(BinlogEventNotifier.class);



    @Autowired
    private AppConfiguration appConfiguration;


    @Autowired
    private AppContextProvider appContextProvider;


    private BinlogPosClient binlogPosClient;

    @EventListener
    public void process(BinlogPacket event){
        Packet packet = (Packet) event.getPacket();

        // do process
        PacketType packetType = packet.getPacketType();
        PacketHandler packetHandler = getPacketHandler(packetType);

        packetHandler.beforeHandle(packet);
        if (packetHandler.skipHandle(packet, appConfiguration)){
            return;
        }
        packetHandler.handle(packet);
        afterProcess(packet);

    }


    @EventListener
    public void process(ContextStoppedEvent event){
        if (Objects.isNull(binlogPosClient)){
            return;
        }
        try {
            binlogPosClient.close();
        }catch (Exception e){
            LOG.info("error happened when close binlogPosClient", e);
        }
    }


    private void afterProcess(Packet packet){
        // logic for HA mode
        if (StringUtils.isAnyBlank(appConfiguration.getRaftServerList(), appConfiguration.getRaftGroupId())){
            return;
        }
        BinlogPosition currentPosition = appContextProvider.getCurrentPosition();
        if (StringUtils.isBlank(currentPosition.getFile()) || Objects.isNull(currentPosition.getPos())){
            return;
        }
        if (Objects.isNull(binlogPosClient)){
            binlogPosClient = new BinlogPosClient(appConfiguration.getRaftServerList(), appConfiguration.getRaftGroupId());
        }
        binlogPosClient.send(currentPosition);
    }


    private PacketHandler<?> getPacketHandler(PacketType packetType){
        switch (packetType){
            case ROTATE_EVENT:
                return AppContextProvider.getBean(RotateEventHandler.class);
            case TABLE_MAP_EVENT:
                return AppContextProvider.getBean(TableMapEventHandler.class);
            case ROWS_QUERY_EVENT:
                return AppContextProvider.getBean(RowQueryEventHandler.class);
            case ANONYMOUS_GTID_EVENT:
                return AppContextProvider.getBean(AnonymousGTIDEventHandler.class);
            case XID_EVENT:
                return AppContextProvider.getBean(XidEventHandler.class);
            case QUERY_EVENT:
                return AppContextProvider.getBean(QueryEventHandler.class);
            case DELETE_ROWS_EVENTv2:
                return AppContextProvider.getBean(DeleteRowsEventV2Handler.class);
            case WRITE_ROWS_EVENTv2:
                return AppContextProvider.getBean(WriteRowsEventV2Handler.class);
            case UPDATE_ROWS_EVENTv2:
                return AppContextProvider.getBean(UpdateRowsEventV2Handler.class);
            default:
                return AppContextProvider.getBean(NoopHandler.class);
        }
    }
}
