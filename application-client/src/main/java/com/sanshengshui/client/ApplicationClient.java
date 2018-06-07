package com.sanshengshui.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 穆书伟
 * @description 客户端
 * @date 2018/6/7 16:24
 */
@SpringBootApplication
public class ApplicationClient {
    private final static Logger log = LoggerFactory.getLogger(ApplicationClient.class);
    public static void main(String[] args){
        SpringApplication.run(ApplicationClient.class,args);
    }
}
