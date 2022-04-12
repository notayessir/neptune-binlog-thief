

package com.notayessir.ha;

import com.notayessir.common.BinlogPosition;
import com.notayessir.common.configure.ThiefConfiguration;
import com.notayessir.connector.BaseBinlogThief;
import org.apache.commons.lang3.StringUtils;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftGroupMemberId;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.protocol.TermIndex;
import org.apache.ratis.server.raftlog.RaftLog;
import org.apache.ratis.server.storage.RaftStorage;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.apache.ratis.statemachine.impl.SingleFileSnapshotInfo;
import org.apache.ratis.util.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * binlog 状态机
 */
public class BinlogStateMachine extends BaseStateMachine {


    private static final Logger LOG = LoggerFactory.getLogger(BinlogStateMachine.class);


    private final SimpleStateMachineStorage storage = new SimpleStateMachineStorage();

    private BinlogPosition binlogPosition = new BinlogPosition();

    private BaseBinlogThief binlogDeliverer;

    private final ThiefConfiguration thiefConfiguration;


    public BinlogStateMachine(ThiefConfiguration thiefConfiguration) {
        this.thiefConfiguration = thiefConfiguration;
    }

    /**
     * initialize the state machine by initilize the state machine storage and
     * calling the load method which reads the last applied command and restore it
     * in counter object)
     *
     * @param server      the current server information
     * @param groupId     the cluster groupId
     * @param raftStorage the raft storage which is used to keep raft related
     *                    stuff
     * @throws IOException if any error happens during load state
     */
    @Override
    public void initialize(RaftServer server, RaftGroupId groupId,
                           RaftStorage raftStorage) throws IOException {
        super.initialize(server, groupId, raftStorage);
        this.storage.init(raftStorage);
        load(storage.getLatestSnapshot());
    }

    /**
     * very similar to initialize method, but doesn't initialize the storage
     * system because the state machine reinitialized from the PAUSE state and
     * storage system initialized before.
     *
     * @throws IOException if any error happens during load state
     */
    @Override
    public void reinitialize() throws IOException {
        load(storage.getLatestSnapshot());
    }

    /**
     * Store the current state as an snapshot file in the stateMachineStorage.
     *
     * @return the index of the snapshot
     */
    @Override
    public long takeSnapshot() {
        //get the last applied index
        final TermIndex last = getLastAppliedTermIndex();

        //create a file with a proper name to store the snapshot
        final File snapshotFile =
                storage.getSnapshotFile(last.getTerm(), last.getIndex());

        //serialize the counter object and write it into the snapshot file
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(snapshotFile)))) {
            out.writeObject(binlogPosition);
        } catch (IOException ioe) {
            LOG.warn("Failed to write snapshot file \"" + snapshotFile
                    + "\", last applied index=" + last);
        }

        //return the index of the stored snapshot (which is the last applied one)
        return last.getIndex();
    }

    /**
     * Load the state of the state machine from the storage.
     *
     * @param snapshot to load
     * @return the index of the snapshot or -1 if snapshot is invalid
     * @throws IOException if any error happens during read from storage
     */
    private long load(SingleFileSnapshotInfo snapshot) throws IOException {
        //check the snapshot nullity
        if (snapshot == null) {
            LOG.warn("The snapshot info is null.");
            return RaftLog.INVALID_LOG_INDEX;
        }

        //check the existence of the snapshot file
        final File snapshotFile = snapshot.getFile().getPath().toFile();
        if (!snapshotFile.exists()) {
            LOG.warn("The snapshot file {} does not exist for snapshot {}",
                    snapshotFile, snapshot);
            return RaftLog.INVALID_LOG_INDEX;
        }

        //load the TermIndex object for the snapshot using the file name pattern of
        // the snapshot
        final TermIndex last =
                SimpleStateMachineStorage.getTermIndexFromSnapshotFile(snapshotFile);

        //read the file and cast it to the AtomicInteger and set the counter
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(snapshotFile)))) {
            //set the last applied termIndex to the termIndex of the snapshot
            setLastAppliedTermIndex(last);

            //read, cast and set the counter
            binlogPosition = JavaUtils.cast(in.readObject());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        return last.getIndex();
    }



    /**
     * Apply the INCREMENT command by incrementing the counter object.
     *
     * @param trx the transaction context
     * @return the message containing the updated counter value
     */
    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();
        String logData = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset());

        // update the last applied term and index
        long index = entry.getIndex();
        updateLastAppliedTermIndex(entry.getTerm(), index);

        // data format: binlog.xxx-987654
        String [] arr = logData.split("-");
        binlogPosition.setFile(arr[0]);
        binlogPosition.setPos(Long.parseLong(arr[1]));
        return CompletableFuture.completedFuture(Message.valueOf(String.valueOf(index)));
    }

    @Override
    public void notifyLeaderChanged(RaftGroupMemberId groupMemberId, RaftPeerId newLeaderId) {
        if (groupMemberId.getPeerId().getRaftPeerIdProto().getId() != newLeaderId.getRaftPeerIdProto().getId()){
            return;
        }
        LOG.info("leadership, id {}", newLeaderId);
        // I'm the leader now
        release();
        if(StringUtils.isNotBlank(binlogPosition.getFile()) && Objects.nonNull(binlogPosition.getPos())){
            thiefConfiguration.setBinlogFilename(binlogPosition.getFile());
            thiefConfiguration.setBinlogStartPos(binlogPosition.getPos());
        }
        binlogDeliverer = new BaseBinlogThief(thiefConfiguration);
        try {
            binlogDeliverer.start();
        }catch (Exception e){
            LOG.info("exception happened when start binlogDeliverer", e);
        }
    }

    @Override
    public void notifyConfigurationChanged(long term, long index, RaftProtos.RaftConfigurationProto newRaftConfiguration) {
        LOG.info("notifyConfigurationChanged, term {}, index {}, newRaftConfiguration {}", term, index, newRaftConfiguration);
    }

    @Override
    public void notifyLogFailed(Throwable cause, RaftProtos.LogEntryProto failedEntry) {
        LOG.warn("notifyLogFailed failedEntry: {}", failedEntry, cause);
    }

    @Override
    public void notifyExtendedNoLeader(RaftProtos.RoleInfoProto roleInfoProto) {
        LOG.warn("notifyExtendedNoLeader: {}", roleInfoProto);
        release();
    }


    @Override
    public void notifyFollowerSlowness(RaftProtos.RoleInfoProto roleInfoProto) {
        LOG.warn("slower follower: {}", roleInfoProto);
    }

    @Override
    public void notifyNotLeader(Collection<TransactionContext> pendingEntries) throws IOException {
        LOG.warn("notifyNotLeader: {}", pendingEntries);
        release();
    }


    @Override
    public void close() {
        release();
    }

    private void release(){
        if (Objects.isNull(binlogDeliverer)){
            return;
        }
        try {
            binlogDeliverer.stop();
        }catch (Exception e){
            LOG.error("exception happened when close server", e);
        }
    }


}
