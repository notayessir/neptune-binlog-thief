package com.notayessir.ha;

import com.notayessir.connector.BinlogThief;
import com.notayessir.common.configure.ThiefConfiguration;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HABinlogThief implements BinlogThief {


    private static final Logger LOG = LoggerFactory.getLogger(HABinlogThief.class);



    private RaftServer server;

    public final List<RaftPeer> peers;

    public final RaftGroup raftGroup;

    private final ThiefConfiguration thiefConfiguration;

    private final BinlogStateMachine stateMachine;


    public HABinlogThief(ThiefConfiguration configuration) {
        // node_1
        String [] node = configuration.getNodeName().split("_");
        thiefConfiguration = configuration;
        thiefConfiguration.setPeerId(Integer.parseInt(node[1]));

        // parse addresses
        String[] addresses = thiefConfiguration.getRaftServerList().split(",");
        List<RaftPeer> list = new ArrayList<>(addresses.length);
        for (int i = 0; i < addresses.length; i++) {
            list.add(RaftPeer.newBuilder().setId(node[0] + i).setAddress(addresses[i]).build());
        }
        peers = Collections.unmodifiableList(list);

        // create raft group
        UUID groupId = UUID.fromString(thiefConfiguration.getRaftGroupId());
        raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(groupId), peers);

        // create stateMachine
        stateMachine = new BinlogStateMachine(thiefConfiguration);

    }

    @Override
    public void start() {
        // start raft server
        RaftProperties properties = new RaftProperties();
        RaftPeer peer = peers.get(thiefConfiguration.getPeerId());

        File storageDir = new File("../ha/" + peer.getId());
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(storageDir));

        int port = NetUtils.createSocketAddr(peer.getAddress()).getPort();
        GrpcConfigKeys.Server.setPort(properties, port);

        try {
            server = RaftServer.newBuilder()
                    .setGroup(raftGroup)
                    .setProperties(properties)
                    .setServerId(peer.getId())
                    .setStateMachine(stateMachine)
                    .build();
            server.start();
        } catch (IOException e) {
            LOG.error("exception happened when build server", e);
        }
    }

    @Override
    public void stop() {
        if (Objects.isNull(server)){
            return;
        }
        try {
            server.close();
            stateMachine.close();
        } catch (IOException e) {
            LOG.error("exception happened when close server", e);
        }

    }
}
