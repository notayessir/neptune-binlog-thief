## 介绍

![architecture](https://github.com/notayessir/neptune-binlog-thief/blob/master/asset/architecture.png)

增量订阅 MySQL binlog 日志，并将事件生产到常用的中间件；基于 Java 8，MySQL 版本 >= 5.7；

功能点：

1. 能够订阅 MySQL 5.7 以上的 binlog 事件；
2. 实现了日志推送到 Redis、Rocket MQ、Kafka；
3. 使用 Raft 协议实现高可用，基于 Apache Ratis；

## 快速开始

### 下载

下载压缩包并解压：

https://github.com/notayessir/neptune-binlog-thief/releases/download/neptune-binlog-thief-0.0.1-beta/neptune-binlog-thief-0.0.1-beta.zip

### 配置

创建 MySQL 账号并授权：

```mysql
mysql> CREATE USER 'binlog_thief'@'%' IDENTIFIED WITH mysql_native_password BY '<password>';
mysql> CREATE USER 'binlog_thief'@'localhost' IDENTIFIED WITH mysql_native_password BY '<password>';
mysql> GRANT SELECT, REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'binlog_thief'@'%';
mysql> GRANT SELECT, REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'binlog_thief'@'localhost';
```

配置 application.properties：

```
spring.datasource.url=jdbc:mysql://localhost:3306/{arbitrary_database}
spring.datasource.username=binlog_thief
spring.datasource.password=xxxxx
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 启动

UNIX：

```shell
cd bin
# 启动 
sh command.sh binlog-service start
# 停止
sh command.sh binlog-service stop
```

Windows：

TODO

### 示例

```mysql
mysql> INSERT INTO `binlog_data`.`t_user` (`name`, `age`, `birth_date`, `sex`, `height`) VALUES ('jack', 30, '2022-04-13 00:38:42', 'female', 80.3)
JSON: 
{
    "data":{
        "name":"jack",
        "id":4,
        "age":30,
        "birth_date":1649781522000,
        "sex":"female",
        "height":80.3
    },
    "eventHeader":{
        "eventSize":68,
        "eventType":30,
        "flags":0,
        "logPos":436131,
        "serverId":1,
        "timestamp":1649781540
    },
    "packetHeader":{
        "payloadLength":69,
        "sequenceId":7
    },
    "packetType":"WRITE_ROWS_EVENTv2",
    "tableMap":{
        "columnDefs":[
            {
                "columnType":"MYSQL_TYPE_LONGLONG",
                "metadata":"",
                "nullable":false,
                "pos":0
            },
            {
                "columnType":"MYSQL_TYPE_VARCHAR",
                "metadata":"/QI=",
                "nullable":true,
                "pos":1
            },
            {
                "columnType":"MYSQL_TYPE_LONG",
                "metadata":"",
                "nullable":true,
                "pos":2
            },
            {
                "columnType":"MYSQL_TYPE_DATETIME2",
                "metadata":"AA==",
                "nullable":true,
                "pos":3
            },
            {
                "columnType":"MYSQL_TYPE_STRING",
                "metadata":"9wE=",
                "nullable":true,
                "pos":4
            },
            {
                "columnType":"MYSQL_TYPE_DOUBLE",
                "metadata":"CA==",
                "nullable":true,
                "pos":5
            }
        ],
        "schemaName":"binlog_data",
        "tableId":256,
        "tableName":"t_user"
    },
    "xid":42798
}
mysql> UPDATE `binlog_data`.`t_user` SET `name` = 'jason', `sex` = 'male', `height` = 90 WHERE `id` = 4
JSON: 
{
    "data":{
        "name":"jason",
        "id":4,
        "age":30,
        "birth_date":1649781522000,
        "sex":"male",
        "height":90
    },
    "eventHeader":{
        "eventSize":103,
        "eventType":31,
        "flags":0,
        "logPos":436628,
        "serverId":1,
        "timestamp":1649781707
    },
    "oldData":{
        "name":"jack",
        "id":4,
        "age":30,
        "birth_date":1649781522000,
        "sex":"female",
        "height":80.3
    },
    "packetHeader":{
        "payloadLength":104,
        "sequenceId":13
    },
    "packetType":"UPDATE_ROWS_EVENTv2",
    "tableMap": // ignore
    "xid":42803
}
mysql> DELETE FROM `binlog_data`.`t_user` WHERE `id` = 4
JSON：
{
    "data":{
        "name":"jason",
        "id":4,
        "age":30,
        "birth_date":1649781522000,
        "sex":"male",
        "height":90
    },
    "eventHeader":{
        "eventSize":69,
        "eventType":32,
        "flags":0,
        "logPos":437035,
        "serverId":1,
        "timestamp":1649781780
    },
    "packetHeader":{
        "payloadLength":70,
        "sequenceId":19
    },
    "packetType":"DELETE_ROWS_EVENTv2",
    "tableMap": // ignore
    "xid":42806
}
```

## 更多配置

### 数据库

```
# MySQL 服务器，任意填一个数据库
spring.datasource.url=jdbc:mysql://localhost:3306/binlog_data
# 用户名
spring.datasource.username=maxwell
# 密码
spring.datasource.password=123456
# MySQL 驱动
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### binlog

```
# 起始 binlog 文件，不填则按照当前数据库所在位置
app.binlog.filename=binlog.000028
# 起始 binlog 位置，不填则按照当前数据库所在位置
app.binlog.start.pos=426321
```

### Kafka

```
# Kafka 主题前缀，例如 packetType=WRITE_ROWS_EVENTv2，推送到 Kafka 的主题为 xxxWRITE_ROWS_EVENTv2
app.producer.kafka.topic.prefix=
# 集群地址
#app.producer.kafka.nameserver=211.72.207.131:9092
```

### Rocket MQ

```
# mq 主题
app.producer.rocketmq.topic=Neptune-Deliverer-Binlog
# mq nameserver
app.producer.rocketmq.nameserver=localhost:9876
```

### Redis

```
# Redis 直连 host
app.producer.redis.host=localhost
# Redis 直连 port
app.producer.redis.port=6379
```

### 库、表过滤

```
# 需要过滤的库和表
app.binlog.ignoredTable=
```

例如：

- 需要过滤整个 data 库：app.binlog.ignoredTable=data:*
- 过滤多个库：app.binlog.ignoredTable=data1:\*;data2:\*;
- 过滤库的多个表：app.binlog.ignoredTable=data1:t1,t2,t3;data2:u1,u2,u3

### 高可用

```
# 集群地址，当这两个字段不为空时，使用高可用模式
raft.server.list=127.0.0.1:10024,127.0.0.1:10124,127.0.0.1:11124
# group id
raft.group.id=02511d47-d67c-49a3-9011-abb3109a44c1
```