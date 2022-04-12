package com.notayessir.common.packet;


public class HandshakeRespPacket extends Packet implements Computable {

    private final byte [] capabilityFlags;
    private final byte [] maxPacketSize;
    private final byte characterSet;
    private final byte [] reserved;
    private final byte [] username;
    private final byte authResponseLen;
    private final byte [] authResponse;

//    private String database;
//    private String authPluginName;
//    private int keyValueLen;
//    private String key;
//    private String value;

    public HandshakeRespPacket(PacketHeader packetHeader, byte [] username, byte[] authResponse) {
        this.packetHeader = packetHeader;
        // 功能标志
        capabilityFlags = new byte[]{0x05, (byte) 0xa6, 0x0b, 0x00};
        // 包的最大字节数
        maxPacketSize = new byte[]{0x00,  0x00, 0x00, 0x01};
        // 字符固定 utf-8
        characterSet = 33;
        // 保留 23 byte
        reserved = new byte[23];
        this.username = new byte[username.length + 1];
        System.arraycopy(username, 0, this.username, 0,  username.length);
        this.authResponse = authResponse;
        this.authResponseLen = (byte) authResponse.length;
        this.packetHeader.setPayloadLength(compute());
    }

    @Override
    public int compute() {
        return capabilityFlags.length + maxPacketSize.length + 1
        + reserved.length + username.length + 1 + authResponseLen;
    }


    public byte[] getCapabilityFlags() {
        return capabilityFlags;
    }

    public byte[] getMaxPacketSize() {
        return maxPacketSize;
    }

    public byte[] getUsername() {
        return username;
    }



    public byte getCharacterSet() {
        return characterSet;
    }

    public byte[] getReserved() {
        return reserved;
    }


    public byte getAuthResponseLen() {
        return authResponseLen;
    }

    public byte[] getAuthResponse() {
        return authResponse;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.HANDSHAKE_RESP_PACKET;
    }
}
