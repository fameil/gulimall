package com.srz.mes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author srz
 * @create 2022/8/28 22:18
 */
@MapperScan("com.srz.mes.dao")
@SpringBootApplication
public class mesMain8081 {
    public static void main(String[] args) {
        SpringApplication.run(mesMain8081.class,args);
    }
}
