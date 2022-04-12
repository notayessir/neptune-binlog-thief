package com.notayessir.common.util;


import com.notayessir.common.packet.CapabilityFlag;

public class CapabilityUtil {

    public static boolean isClientPluginAuth(int capabilityFlag){
        return (CapabilityFlag.CLIENT_PLUGIN_AUTH.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_PLUGIN_AUTH.getVal();
    }

    public static boolean isClientSecureConn(int capabilityFlag){
        return (CapabilityFlag.CLIENT_SECURE_CONNECTION.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_SECURE_CONNECTION.getVal();
    }

    public static boolean isClientProtocol41(int capabilityFlag){
        return (CapabilityFlag.CLIENT_PROTOCOL_41.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_PROTOCOL_41.getVal();
    }

    public static boolean isClientPluginAuthLenEncClientData(int capabilityFlag){
        return (CapabilityFlag.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA.getVal();
    }

    public static boolean isClientConnectWithDB(int capabilityFlag){
        return (CapabilityFlag.CLIENT_CONNECT_WITH_DB.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_CONNECT_WITH_DB.getVal();
    }

    public static boolean isClientConnectAttrs(int capabilityFlag){
        return (CapabilityFlag.CLIENT_CONNECT_ATTRS.getVal() & capabilityFlag) == CapabilityFlag.CLIENT_CONNECT_ATTRS.getVal();
    }
}
