package com.srz.gulimall.product.web;

import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import com.srz.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author srz
 * @create 2022/12/29 15:00
 */
@Controller
public class IndexController {

    @Autowired
    PmsCategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        //TODO 1、查出所有的1级分类
        List<PmsCategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        //视图解析器进行拼串:
        // classpath:/templates/ +返回值+.htmL
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    //"index/catalog.json"
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        //1、获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //2、加锁
        lock.lock();//阻塞式等待。默认加的锁都是30s时间。
        //1、锁的自动续期，如果业务超长，运行自动给锁续上新的30s，
        //   不用担心业务时间长锁自动过期被删掉
        //2、加锁的业务只要运行完成，就当前锁续期，即使不手动解锁，锁默认在30s以后自动删除。
        //1、如果我们传递了锁的超时时间，就发送为redis执行脚本，进行占锁，超时时间为指定的时间
        //2、如果我们未指定超时时间，就使用lockWatchdogTimeout = 30000L;看门狗默认时间
        //3、只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗默认时间】
        //internalLockLeaseTime【看门狗时间】 / 3L;10s以后续期
        //相当于每隔10s自动续期成为30s

        //lock.lock(10, TimeUnit.SECONDS);//10秒自动解锁，自动解锁时间一定要大于业务的执行时间
        //lock.lock(10, TimeUnit.SECONDS);在锁时间到了以后，不会自动续期。

        //最佳实战
        //lock.lock(30, TimeUnit.SECONDS);//省掉了整个续期操作。手动解锁
        try {
            System.out.println(Thread.currentThread().getId()+"加锁成功，执行业务.....");
            Thread.sleep(30000);
        }catch (Exception e){

        }finally {
            //解锁
            System.out.println(Thread.currentThread().getId()+"释放锁...");
            lock.unlock();
        }


        return "hello";
    }

    //保证一定能读到最新数据
    //修改期间，写锁是一个排他锁（互斥锁，独享锁），读锁是一个共享锁。
    //没释放读就必须等待
    //写 + 读：等待写锁释放
    //写 + 写：阻塞方式
    //读 + 读：相当于无锁，只会在redis中记录好，他们会同时加锁成功
    //读 + 写：等读锁释放
    //结论只要有写的存在，都必须等待
    @ResponseBody
    @GetMapping("/write")
    public String writeValue(){
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        RLock rLock = readWriteLock.writeLock();
        String s = "";
        try {
            //1、改数据加写锁，读数据加读锁;
            rLock.lock();
            System.out.println(Thread.currentThread().getId()+"写锁加锁成功.....");
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue",s);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            rLock.unlock();
            System.out.println(Thread.currentThread().getId()+"写锁释放");
        }
        return s;
    }

    @ResponseBody
    @GetMapping("/read")
    public String readValue(){
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        RLock rLock = readWriteLock.readLock();
        String writeValue = "";
        rLock.lock();
        System.out.println(Thread.currentThread().getId()+"读锁加锁成功.....");
        try {
            Thread.sleep(30000);
            writeValue = redisTemplate.opsForValue().get("writeValue");
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            rLock.unlock();
            System.out.println(Thread.currentThread().getId()+"读锁释放");
        }
        return writeValue;
    }

    @ResponseBody
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        //park.acquire();//获取一个信号，获取一个值,占一个车位
        boolean b = park.tryAcquire();


        return b?"ok":"没有停车位";
    }

    @ResponseBody
    @GetMapping("/go")
    public String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release();//释放一个车位


        return "ok";
    }
    /*
    * 班级全部人走完了可以锁门
    * */
    @ResponseBody
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁都完成

        return "放假了...";
    }

    @ResponseBody
    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();//计数减一

        //CountDownLatch

        return id+"号同学走了。。。";
    }
}
