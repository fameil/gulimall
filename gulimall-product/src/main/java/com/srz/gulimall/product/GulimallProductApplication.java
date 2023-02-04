package com.srz.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author srz
 * @create 2022/9/8 0:42
 */
//2、逻辑删除
//        1)、配置全局的逻辑删除规则(省略)
//        2)、配置逻辑删除的组件Bean (省略)
//        3)、给Bean加上逻辑删除注解@TableLogic
//3、JSR303
//        1)、给Bean添加校验注解:javax. validation. constraints, 并定义自己的message提示
//        2)、开启校验功能@Valid
//             效果:校验错误以后会有默认的响应;
//        3)、给校验的bean后紧跟一个BindingResult, 就可以获取到校验的结果
//        4)、分组校验
//            1)、@NotBlank(message = "品牌名必须提交",groups = {AddGroup. class, UpdateGroup.class})
//              给校验注解标注什么情况需要进行校验
//            2)、@Validated({AddGroup.class})
//            3)、默认没有指定分组的校验注解@NotBlank,在分组校验情况下不生效，只会在@Validated生效;
//        5、统一的异常处理
//          @ControllerAdvice
//        )、自定义校验
//            1)、编写一个自定义的校验注解
//            2)、编写一个自定义的校验器
//        ConstraintVal idator
//            3)、关联自定义的校验器和自定义的校验注解
//        @Documented
//        @Constraint(validatedBy = { ListValueConstraintVal idator. class [可以指定多个不同的校验器]
//        @Target({ METHOD, FIELD, ANNOTATION_ TYPE, CONS TRUCTOR, PARAMETER, TYPE_ _USE })
//        @Retention(RUNTIME)
//        public @interface ListValue {
//        4、统一的异常处理
//        @Control LerAdvice
//        1)、编写异常处理类，使用@ControllerAdvice.
//        2)、使用@ExceptionHandler标注方法可以处理的异常。

//    5、模板引擎.
//            1)、thymeleaf-starter: 关闭缓存
//            2)、静态资源都放在static文件夹下就可以按照路径直接访问
//            3)、页面放在templates下，直接访问
//            SpringBoot,访问项目的时候，默认会找index
//            4)、页面修改不重启服务器实时更新
//            1)、引入dev-tools
//          2)、修改完页面controller shift f9重新自动编译下页面，代码配置，推荐重启
/*

    6、整合redis
        1)、引入data-redis-starter
        2)、简单配置redis的host等信息
        3)、使用SpringBoot自动配置好的StringRedis Template来操作redis
        redis-》Map;存放数据key,数据值value
    7、整合redisson作为分布式锁等功能框架
        1)、引入依赖
        <!-- https://mvnrepository.com/artifact/org.redisson/redisson -->
            <dependency>
                <groupId>org.redisson</groupId>
                <artifactId>redisson</artifactId>
                <version>3.18.0</version>
            </dependency>
        2)、配置redisson
    8、整合SpringCache简化缓存开发
        1)、引入依赖
            spring-boot-starter-cache、spring-boot-starter-data-redis
        2）、写配置
            1.自动配置了哪些
            CacheAutoConfiguration会导入RedisCacheConfiguration
            自动配好了缓存管理器RedisCacheManager
            2.配置使用redis作为缓存
        3）、使用缓存
            @Cacheable：触发将数据保存到缓存。
            @CacheEvict：触发将数据从缓存删除。
            @CachePut：在不干扰方法执行的情况下更新缓存。
            @Caching：组合以上多个操作;
            @CacheConfig：在类级别共享一些常见的缓存相关设置。
            1.开启缓存功能@EnableCaching
            2.只需要使用注解就能完成缓存操作
            CacheAutoConfiguration -> RedisCacheConfiguration ->
            自动配置RedisCacheManager-> 初始化所有的缓存->每个缓存决定使用什么配置->
            如果配置有就用已有的，没有就用默认配置-> 想改缓存的配置，
            只需要给容器中放一个RedisCacheConfiguration即可->
            就会应用到当前RedisCacheManage管理的所有缓存分区中

*
* */




@EnableFeignClients(basePackages = "com.srz.gulimall.product.feign")
@MapperScan("com.srz.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class,args);
    }
}
