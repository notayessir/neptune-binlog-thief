package com.notayessir.bootstrap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan(basePackages = {"com.notayessir"})
@SpringBootApplication
public class NeptuneApplication {

    private static final Logger LOG = LoggerFactory.getLogger(NeptuneApplication.class);



    public static void main(String[] args) {
        try {
            SpringApplication.run(NeptuneApplication.class, args);
        }catch (Exception e){
            LOG.error("exception happened when run application: ", e);
        }

    }

}
