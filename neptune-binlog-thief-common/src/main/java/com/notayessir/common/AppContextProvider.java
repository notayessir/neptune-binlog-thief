package com.notayessir.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *  应用 context，提供获取 spring bean 入口
 */
@Component
public class AppContextProvider implements ApplicationContextAware {


    private static ApplicationContext appContext;


    /**
     * 当前读取到的 binlog 实时位置
     */
    private final BinlogPosition currentPosition = new BinlogPosition();




    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    /**
     * 根据类获取 bean
     * @param clz   class
     * @param <C>   范型
     * @return      spring bean
     */
    public static <C> C getBean(Class<C> clz){
        return appContext.getBean(clz);
    }

    /**
     * 获取当前 binlog 位置
     * @return  binlog 位置
     */
    public BinlogPosition getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 更新 binlog 位置
     * @param pos   binlog 位置
     */
    public void setCurrentBinlogPos(long pos) {
        currentPosition.setPos(pos);
    }

    /**
     * 更新 binlog 文件位置
     * @param file      文件位置
     */
    public void setCurrentBinlogFile(String file) {
        currentPosition.setFile(file);
    }

}
