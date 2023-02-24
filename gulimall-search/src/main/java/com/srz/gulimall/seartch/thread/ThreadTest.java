package com.srz.gulimall.seartch.thread;

import java.util.concurrent.*;

/**
 * @author srz
 * @create 2023/2/22 5:22
 */
public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");

//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//        }, executor);

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, executor);

        Integer integer = future.get();

        System.out.println("main...end..."+integer);
    }


    public static void threads(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
        /*
            * 1)、继承Thread
            * 2)、实现Runnable接口
            * 3)、实现Callable接口 + FutureTaske (可以拿到返回结果，可以处理异常)
            * 4)、线程池；给线程池直接提交任务。
            */

        //1、继承Thread
        //new Thread01().start();

        //2、实现Runnable接口
        //new Thread(new Runable01()).start();

        //3、实现Callable接口 + FutureTaske
/*        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        new Thread(futureTask).start();
        //阻塞等待整个线程执行完成，获取返回结果
        Integer integer = futureTask.get();
        System.out.println("main...end..."+integer);
        */

        //我们以后在业务代码里面，以上三种启动线程的方式都不用。
        //区别：
        // 1、2方式不能得到返回值3方式可以获取返回值
        // 1、2、3方式都不能控制资源
        // 4可以控制资源, 性能稳定

        //将所有的多线程异步任务都交给线程池执行
        //当前系统中池只有一两个，每个异步任务，提交给线程池让他自己去执行就行
/*        //4、线程池
        //给线程池直接提交任务
        service.submit(new Runable01());

        Future: 可以获取到异步结果


        */


        /*七大参数:
        *corePoolSize: [5] 核心线程数：线程池，创建好以后就准备就绪线程数量，
        *就等待来接受异步任务就执行。线程一直存在除非设置了allowCoreThreadTimeOut
        *       5个 Thread thread = new Thread(); thread.start();
        *
        *maximumPoolSize: [200] 最大线程数量；控制资源
        *
        *keepAliveTime: 存活时间。非核心线程的活跃时间。当线程超过核心线程数时，
        *会额外创建非核心线程，执行完任务，等待时间结束后，非核心线（maximumPoolSize-corePoolSize）程销毁。
        *
        *TimeUnit: 时间单位
        *
        *BlockingQueue<Runnable> workRueue: 阻塞队列，如果任务有很多，就会将目前的任务放在
        *队列里面。只要有线程空间,就会去队列里面取出新的任务继续执行。
        *
        *ThreadFactory: 线程的创建工厂
        *
        *RejectedExecutionHandler: 如果队列满了,按照我们指定的拒绝策略拒绝任务
        *
        *工作顺序:
        *   1、线程池创建，准备好core数量的核心线程，准备接受任务
        *   1.1、core满了，就将再的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取
        *   1.2、阻塞队列满了，就直接开新线程执行，最大中能开到max指定的数量
        *   1.3、max满了就用RejectedExecutionHandler拒绝策略拒绝任务
        *   1.4、max都执行完成，有很多空闲,指定空闲时间keepAliveTime过后释放非核心线程
        *       new LinkedBlockingDeque<>():默认是Integer的最大值。内在不够
        *   一个线程池core 7;
        *   max.20，queue: 50，100并 发进来怎么分配的;
        *   7个会立即得到执行，50个会进入队列，再开13个进行执行。剩下的30个就使用拒绝策略。
        *   如果不想抛弃还要执行。CallerRunsPolicy;
        * */

        //Executors.newCachedThreadPool() core是0，所有线程都可回收
        //Executors.newFixedThreadPool() 固定大小，core=max;都不可回收
        //Executors.newScheduledThreadPool() 定时任务的线程池
        //Executors.newSingleThreadExecutor() 单线程的线程池,后台从队列里面获取任务

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());


        System.out.println("main...end...");
    }

    public static class Thread01 extends Thread{

        @Override
        public void run() {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果："+i);

        }
    }

    public static class Runable01 implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果："+i);
        }
    }

    public static class Callable01 implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果："+i);
            Thread.sleep(3000);
            return i;
        }
    }
}
