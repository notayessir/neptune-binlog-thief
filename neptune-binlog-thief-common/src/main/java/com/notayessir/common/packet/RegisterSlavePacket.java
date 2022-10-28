package com.notayessir.common.packet;


import com.notayessir.common.util.ByteUtil;

/**
 * 注册成为子节点数据帧
 */
public class RegisterSlavePacket extends Packet implements Computable {

    private final byte registerSlave;

    private final byte[] serverId;

    private final byte slaveHostNameLen;

    private final byte[] slaveHostName;

    private final byte slaveUserLen;

    private final byte[] slaveUser;

    private final byte slavePassLen;

    private final byte[] slavePass;

    private final byte [] mysqlPort;

    private final byte [] repRank;

    private final byte [] masterId;


    public RegisterSlavePacket(PacketHeader packetHeader, int serverId) {
        this.packetHeader = packetHeader;
        this.registerSlave = 21;
        this.serverId = ByteUtil.intToBytes(serverId);


        this.slaveHostNameLen = 0;
        this.slaveHostName = new byte[0];

        this.slaveUserLen = 0;
        this.slaveUser = new byte[0];

        this.slavePassLen = 0;
        this.slavePass = new byte[0];

        this.mysqlPort = new byte[]{0, 0};
        this.repRank = ByteUtil.intToBytes(0);
        this.masterId = ByteUtil.intToBytes(0);
        this.packetHeader.setPayloadLength(compute());
    }

    @Override
    public int compute() {
        return 1 + serverId.length
                + 1 + slaveHostName.length
                + 1 + slaveUser.length
                + 1 + slavePass.length
                + mysqlPort.length
                + repRank.length
                + masterId.length;
    }

    public byte getRegisterSlave() {
        return registerSlave;
    }

    public byte[] getServerId() {
        return serverId;
    }

    public byte getSlaveHostNameLen() {
        return slaveHostNameLen;
    }

    public byte getSlaveUserLen() {
        return slaveUserLen;
    }


    public byte getSlavePassLen() {
        return slavePassLen;
    }


    public byte[] getMysqlPort() {
        return mysqlPort;
    }

    public byte[] getRepRank() {
        return repRank;
    }

    public byte[] getMasterId() {
        return masterId;
    }

    public byte[] getSlaveHostName() {
        return slaveHostName;
    }

    public byte[] getSlaveUser() {
        return slaveUser;
    }

    public byte[] getSlavePass() {
        return slavePass;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.REGISTER_SLAVE_PACKET;
    }
}
