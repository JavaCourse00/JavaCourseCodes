package io.github.kimmking.gateway;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        System.out.println(Runtime.getRuntime().availableProcessors());
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        ThreadPoolExecutor proxyService = new ThreadPoolExecutor(cores,
                cores,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"),
                handler);
        return proxyService;
    }
}
