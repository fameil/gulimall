package com.srz.gulimall.ware.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author srz
 * @create 2022/12/3 1:23
 */
@EnableTransactionManagement
@MapperScan("com.srz.gulimall.ware.dao")
@Configuration
public class WareMyBatisConfig {
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//
//        属性名	类型	默认值	描述
//        overflow	boolean	false	溢出总页数后是否进行处理(默认不处理,参见 插件#continuePage 方法)
//        maxLimit	Long		单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
//        dbType	DbType		数据库类型(根据类型获取应使用的分页方言,参见 插件#findIDialect 方法)
//        dialect	IDialect		方言实现类(参见 插件#findIDialect 方法)

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setMaxLimit(100L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}
