package com.notayessir.bootstrap.listener;

import com.notayessir.connector.BaseBinlogThief;
import com.notayessir.connector.BinlogThief;
import com.notayessir.common.configure.ThiefConfiguration;
import com.notayessir.ha.HABinlogThief;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.database.entity.MasterStatusEntity;
import com.notayessir.processor.database.repository.ColumnRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Objects;


/**
 * 根据 application.properties 配置启动 binlog 订阅
 */
@Component
public class BinlogThiefBootstrap implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(BinlogThiefBootstrap.class);


    @Autowired
    private ColumnRepository columnRepository;

    @Autowired
    private AppConfiguration appConfiguration;

    private BinlogThief thief;



    @Override
    public void run(String... args) throws Exception {
        // 读取应用名称，若使用高可用模式，应用名称需要遵循 {name}_{n}，例如 appA_0,appB_1,appC_2
        String nodeName = args[0];
        ThiefConfiguration configuration = buildDelivererConfiguration(nodeName);
        if (StringUtils.isAnyBlank(configuration.getRaftServerList(), configuration.getRaftGroupId())){
            thief = new BaseBinlogThief(configuration);
        } else {
            // HA, accept peer id from command line
            thief = new HABinlogThief(configuration);
        }
        thief.start();
    }


    /**
     * 构建 binlog slave 应用配置
     * @param nodeName  应用名称
     * @return          slave 应用配置
     */
    private ThiefConfiguration buildDelivererConfiguration(String nodeName){
        // set replica binlog start position
        if (StringUtils.isBlank(appConfiguration.getBinlogFilename())
                || Objects.isNull(appConfiguration.getBinlogStartPos())) {
            MasterStatusEntity masterStatus = columnRepository.showMasterStatus();
            appConfiguration.setBinlogFilename(masterStatus.getFile());
            appConfiguration.setBinlogStartPos(masterStatus.getPosition());
        }
        appConfiguration.setNodeName(nodeName);
        // copy properties
        ThiefConfiguration configuration = new ThiefConfiguration();
        BeanUtils.copyProperties(appConfiguration, configuration);
        return configuration;
    }


    @PreDestroy
    public void destroy(){
        if (Objects.isNull(thief)){
            return;
        }
        try {
            thief.stop();
            LOG.info("binlog thief already stopped.");
        }catch (Exception e){
            LOG.error("error happened when stopping the app", e);
        }
    }


}
