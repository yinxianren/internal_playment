package com.internal.playment.pay.component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 // Java线程池的完整构造函数
 ThreadPoolExecutor(

 int corePoolSize, // 线程池长期维持的线程数，即使线程处于Idle状态，也不会回收。
 int maximumPoolSize, // 线程数的上限
 long keepAliveTime, TimeUnit unit, // 超过corePoolSize的线程的idle时长，超过这个时间，多余的线程会被回收。
 BlockingQueue<Runnable> workQueue, // 任务的排队队列
 ThreadFactory threadFactory, // 新线程的产生方式
 RejectedExecutionHandler handler  // 拒绝策略
 )


 三种提交任务的方式：
 Future<T> submit(Callable<T> task) 	是
 void execute(Runnable command) 	否
 Future<?> submit(Runnable task) 	否，虽然返回Future，但是其get()方法总是返回null

 拒绝策略 	                        拒绝行为
 AbortPolicy 	              抛出RejectedExecutionException
 DiscardPolicy 	              什么也不做，直接忽略
 DiscardOldestPolicy 	      丢弃执行队列中最老的任务，尝试为当前提交的任务腾出位置
 CallerRunsPolicy 	          直接由提交任务者执行这个任务
 */

public class ThreadPoolExecutorComponent {


   public static ExecutorService executorService = new ThreadPoolExecutor(30, 60,
            60 * 5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(106), // 使用有界队列，避免OOM
            new ThreadPoolExecutor.CallerRunsPolicy());


}
