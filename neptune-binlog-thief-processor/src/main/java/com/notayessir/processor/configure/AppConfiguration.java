package com.notayessir.processor.configure;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 映射 application.properties
 */
@Component
public class AppConfiguration {


    /**
     * 忽略的数据库和表
     */
    @Value("${app.binlog.ignoredTable:}")
    private String ignoredTable;

    /**
     * binlog 订阅起始文件
     */
    @Value("${app.binlog.filename:}")
    private String binlogFilename;

    /**
     * binlog 订阅起始位置
     */
    @Value("${app.binlog.start.pos:}")
    private Long binlogStartPos;

    /**
     * 高可用服务器列表
     */
    @Value("${raft.server.list:}")
    private String raftServerList;

    /**
     * 高可用组 id
     */
    @Value("${raft.group.id:}")
    private String raftGroupId;

    /**
     * kafka 主题前缀
     */
    @Value("${app.producer.kafka.topic.prefix:}")
    private String kafkaTopicPrefix;

    /**
     * kafka 集群地址
     */
    @Value("${app.producer.kafka.nameserver:}")
    private String kafkaNameserver;

    /**
     * mq 主题
     */
    @Value("${app.producer.rocketmq.topic:}")
    private String rocketmqTopic;


    /**
     * mq 集群地址
     */
    @Value("${app.producer.rocketmq.nameserver:}")
    private String rocketmqNameserver;

    /**
     * redis host
     */
    @Value("${app.producer.redis.host:}")
    private String redisHost;

    /**
     * redis port
     */
    @Value("${app.producer.redis.port:6379}")
    private Integer redisPort;


    /**
     * MySQL username
     */
    @Value("${spring.datasource.username:}")
    private String username;

    /**
     * MySQL password
     */
    @Value("${spring.datasource.password:}")
    private String password;


    @Value("${spring.datasource.url:}")
    private String jdbcURL;

    /**
     * MySQL host
     */
    private String host;

    /**
     * MySQL port
     */
    private int port;

    /**
     * 当前连接所在的数据库
     */
    private String database;

    /**
     * 应用名称
     */
    private String nodeName;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    public String getRaftGroupId() {
        return raftGroupId;
    }

    public String getRaftServerList() {
        return raftServerList;
    }


    public String getHost() {
        if (StringUtils.isBlank(host)){
            String val = StringUtils.split(jdbcURL, "?")[0];
            val = StringUtils.split(val, "/")[1];
            val = StringUtils.split(val, ":")[0];
            host = val;
        }
        return host;
    }

    public int getPort() {
        if (port == 0){
            String val = StringUtils.split(jdbcURL, "?")[0];
            val = StringUtils.split(val, "/")[1];
            val = StringUtils.split(val, ":")[1];
            port = Integer.parseInt(val);
        }
        return port;
    }


    public String getDatabase() {
        if (StringUtils.isBlank(database)){
            String val = StringUtils.split(jdbcURL, "?")[0];
            database = StringUtils.split(val, "/")[2];
        }
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getIgnoredTable() {
        return ignoredTable;
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

    public String getKafkaTopicPrefix() {
        return kafkaTopicPrefix;
    }

    public String getKafkaNameserver() {
        return kafkaNameserver;
    }

    public String getRocketmqTopic() {
        return rocketmqTopic;
    }


    public String getRocketmqNameserver() {
        return rocketmqNameserver;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

}
