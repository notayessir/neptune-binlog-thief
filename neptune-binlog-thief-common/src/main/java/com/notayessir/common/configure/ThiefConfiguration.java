package com.notayessir.common.configure;

/**
 * slave 启动配置 {@link com.notayessir.processor.configure.AppConfiguration}
 */
public class ThiefConfiguration {

    private String host;

    private int port;

    private String username;

    private String password;

    private String raftServerList;

    private String raftGroupId;

    private int peerId;

    private String binlogFilename;

    private Long binlogStartPos;

    private String nodeName;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getBinlogFilename() {
        return binlogFilename;
    }

    public void setBinlogFilename(String binlogFilename) {
        this.binlogFilename = binlogFilename;
    }

    public Long getBinlogStartPos() {
        return binlogStartPos;
    }

    public void setBinlogStartPos(Long binlogStartPos) {
        this.binlogStartPos = binlogStartPos;
    }

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public String getRaftServerList() {
        return raftServerList;
    }

    public void setRaftServerList(String raftServerList) {
        this.raftServerList = raftServerList;
    }

    public String getRaftGroupId() {
        return raftGroupId;
    }

    public void setRaftGroupId(String raftGroupId) {
        this.raftGroupId = raftGroupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
