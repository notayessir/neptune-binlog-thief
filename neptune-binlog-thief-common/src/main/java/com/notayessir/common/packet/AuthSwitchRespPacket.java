package com.notayessir.common.packet;


public class AuthSwitchRespPacket extends Packet implements Computable {

    private final byte [] authPluginResponse;


    public AuthSwitchRespPacket(PacketHeader packetHeader, byte[] authPluginResponse) {
        this.packetHeader = packetHeader;
        this.authPluginResponse = authPluginResponse;
        this.packetHeader.setPayloadLength(compute());
    }

    @Override
    public int compute() {
        return authPluginResponse.length;
    }


    public byte[] getAuthPluginResponse() {
        return authPluginResponse;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.AUTH_SWITCH_RESP_PACKET;
    }
}
