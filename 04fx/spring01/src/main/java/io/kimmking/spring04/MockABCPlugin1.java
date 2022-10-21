package io.kimmking.spring04;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component()
public class MockABCPlugin1 implements ABCPlugin, DisposableBean {

    @Override
    public String toString() {
        System.out.println(Thread.currentThread().getName()+"-"+this.getClass().getSimpleName()+" toString.");
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void startup() throws Exception {
        // mock for netty server start
        System.out.println(Thread.currentThread().getName()+"-"+this.toString()+" started.");
    }

    @Override
    public void shutdown() throws Exception { // 线程池之类可以改进为优雅停机
        System.out.println(Thread.currentThread().getName()+"-"+this.toString()+" stopped.");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println(Thread.currentThread().getName()+"-DisposableBean:" + this.toString());
    }
}
