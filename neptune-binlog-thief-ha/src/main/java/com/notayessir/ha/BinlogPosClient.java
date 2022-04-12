package com.notayessir.ha;

import com.notayessir.common.BinlogPosition;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * raft 客户端，负责提交 binlog 位置
 */
public class BinlogPosClient {

    private static final Logger LOG = LoggerFactory.getLogger(BinlogPosClient.class);


    private final RaftClient raftClient;

    public BinlogPosClient(String raftServerList, String raftGroupId) {
        raftClient = buildClient(raftServerList, raftGroupId);
    }

    /**
     * 创建 raft 客户端
     * @param raftServerList    raft 集群
     * @param raftGroupId       raft 组 id
     * @return
     */
    private static RaftClient buildClient(String raftServerList, String raftGroupId) {
        String[] addresses = raftServerList.split(",");
        List<RaftPeer> list = new ArrayList<>(addresses.length);
        for (int i = 0; i < addresses.length; i++) {
            list.add(RaftPeer.newBuilder().setId("node" + i).setAddress(addresses[i]).build());
        }
        List<RaftPeer> peers = Collections.unmodifiableList(list);

        // create raft group
        UUID groupId = UUID.fromString(raftGroupId);
        RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(groupId), peers);

        RaftProperties raftProperties = new RaftProperties();
        RaftClient.Builder builder = RaftClient.newBuilder()
                .setProperties(raftProperties)
                .setRaftGroup(raftGroup)
                .setClientRpc(new GrpcFactory(new Parameters())
                        .newRaftClientRpc(ClientId.randomId(), raftProperties));
        return builder.build();
    }

    /**
     * 将 binlog 位置发送到 raft 集群
     * @param binlogPosition binlog 位置
     * @return      raft 日志索引
     */
    public Long send(BinlogPosition binlogPosition){
        String message = binlogPosition.getFile() + "-" + binlogPosition.getPos();
        try {
            RaftClientReply reply = raftClient.io().send(Message.valueOf(message));
            return Long.parseLong(reply.getMessage().getContent().toString(Charset.defaultCharset()));
        } catch (IOException e) {
            LOG.error("exception happened when sending message,", e);
        }
        return -1L;
    }

    /**
     * 关闭 raft 客户端，释放相关资源
     */
    public void close(){
        try {
            raftClient.close();
        } catch (IOException e) {
            LOG.info("exception happened when close raft client,", e);
        }
    }




}
