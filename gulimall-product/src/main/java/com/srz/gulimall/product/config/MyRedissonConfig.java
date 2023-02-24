package com.srz.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author srz
 * @create 2023/1/16 14:40
 */

@Configuration
public class MyRedissonConfig {

    /*
    * 所有对Redisson的使用都是通用redissonClient
    * */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        //Redis url should start with redis:// or rediss://
        config.useSingleServer().setAddress("redis://114.115.246.166:6379");
        config.useSingleServer().setPassword("847144514");

        //2、 根据Config，创建出RedissonClien实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
 /*
            //集群模式
            config.useClusterServers()
                .addNodeAddress("127.0.0.1:7004", "127.0.0.1:7001");*/
    }

}
