package io.kimmking.java8;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ParallelDemo {
    
    public static void main(String[] args) {
        testSerialize();
        testParallel();
    }
    
    private static void testSerialize() {
        ArrayList<Object> list = new ArrayList<>(5000000);
        for (int i = 0; i < 5000000; i++) {
            list.add(UUID.randomUUID().toString());
        }
        
        System.out.println("开始排序");
        long startTime = System.nanoTime();//纳秒 比毫秒的精度高
        list.stream().sorted().count();
        
        long endTime = System.nanoTime(); //纳秒, 结束时间
        
        long millis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("串行Stream耗时:" + millis + "毫秒");
    }
    
    private static void testParallel() {
        ArrayList<Object> list = new ArrayList<>(5000000);
        for (int i = 0; i < 5000000; i++) {
            list.add(UUID.randomUUID().toString());
        }
    
        System.out.println("开始排序");
        long startTime = System.nanoTime();//纳秒 比毫秒的精度高
        list.parallelStream().sorted().count();
    
        long endTime = System.nanoTime(); //纳秒, 结束时间
    
        long millis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("并行Stream耗时:" + millis + "毫秒");
    }
    
}
