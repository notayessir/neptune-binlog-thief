package com.notayessir.common.packet;


public class AuthSwitchReqPacket extends Packet implements Responsive {

    private final int status;

    private final String authPluginName;

    private final byte [] authPluginData;

    public AuthSwitchReqPacket(PacketHeader packetHeader, int status, String authPluginName, byte[] authPluginData) {
        this.packetHeader = packetHeader;
        this.status = status;
        this.authPluginName = authPluginName;
        this.authPluginData = new byte[20];
        System.arraycopy(authPluginData, 0, this.authPluginData, 0, authPluginData.length - 1);
    }

    public int getStatus() {
        return status;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public byte[] getAuthPluginData() {
        return authPluginData;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.AUTH_SWITCH_REQ_PACKET;
    }
}
