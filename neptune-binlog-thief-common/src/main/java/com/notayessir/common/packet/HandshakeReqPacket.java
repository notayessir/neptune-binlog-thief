package com.notayessir.common.packet;


/**
 * HandshakeV10
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake">HandshakeV10</a>
 */
public class HandshakeReqPacket extends Packet implements Responsive {

    /**
     * 1 byte: 0x0a
     */
    private final int protocolVersion;

    /**
     *  string.NUL
     */
    private final String serverVersion;

    /**
     * 4 byte
     */
    private final int connectionId;


    /**
     * 8 byte, string.fix_len
     */
    private final byte [] authPluginDataPart1;


    /**
     * 1 byte: 0
     */
//    private byte filler;

    /**
     * lower 2 bytes
     */
//    private final byte [] lowerCapabilityFlag;

    /**
     * 1 byte
     */
    private final int characterSet;

    /**
     * 2 byte
     */
    private final int statusFlag;

    /**
     * upper 2 bytes
     */
//    private final byte [] upperCapabilityFlag;

    /**
     * 1 byte
     */
    private final int authPluginDataLength;

    /**
     * 10 byte, all [00]
     */
//    private String reserved;

    /**
     * MAX(13, length of auth-plugin-data - 8)
     */
    private final byte [] authPluginDataPart2;

    /**
     * string[NUL], auth-plugin name
     */
    private final String authPluginName;

    /**
     * authPluginDataPart1 + authPluginDataPart2
     */
    private final byte [] authPluginData;


    private final int capabilityFlag;


    public HandshakeReqPacket(PacketHeader packetHeader, int protocolVersion, String serverVersion, int connectionId, byte [] authPluginDataPart1,
                              int characterSet, int statusFlag,
                              int authPluginDataLength, byte[] authPluginDataPart2, int capabilityFlag, String authPluginName) {
        this.packetHeader = packetHeader;
        this.protocolVersion = protocolVersion;
        this.serverVersion = serverVersion;
        this.connectionId = connectionId;
        this.authPluginDataPart1 = authPluginDataPart1;
        this.characterSet = characterSet;
        this.statusFlag = statusFlag;
        this.authPluginDataLength = authPluginDataLength;
        this.authPluginDataPart2 = authPluginDataPart2;
        this.authPluginName = authPluginName;
        this.authPluginData = new byte[authPluginDataPart1.length + authPluginDataPart2.length - 1]; // 抛弃 0x00
        System.arraycopy(authPluginDataPart1, 0, authPluginData, 0, authPluginDataPart1.length);
        System.arraycopy(authPluginDataPart2, 0, authPluginData, authPluginDataPart1.length, authPluginDataPart2.length - 1);
        this.capabilityFlag = capabilityFlag;
    }

    public int getCapabilityFlag() {
        return capabilityFlag;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public byte[] getAuthPluginDataPart1() {
        return authPluginDataPart1;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public int getAuthPluginDataLength() {
        return authPluginDataLength;
    }

    public byte[] getAuthPluginDataPart2() {
        return authPluginDataPart2;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public byte[] getAuthPluginData() {
        return authPluginData;
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.HANDSHAKE_REQ_PACKET;
    }
}
