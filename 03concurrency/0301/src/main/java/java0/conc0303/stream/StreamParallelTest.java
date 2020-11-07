package java0.conc0303.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *  parallel 不适合IO密集型  （多线程处理）
 * @author tn
 * @version 1
 * @ClassName StreamParallelTest
 * @description
 * @date 2020/11/7 22:24
 */
public class StreamParallelTest {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue(10000);
        //随机设置值
        IntStream.range(1, 10000).forEach(i -> list.add(i));
        System.out.println("Integer:"+list.toString());


//        List<Long> longList = list.stream().parallel()
//                .map(i -> i.longValue())
//                .sorted()
//                .collect(Collectors.toList());
        List<Long> longList = list.stream().map(i -> i.longValue()).sorted().collect(Collectors.toList());
        long start=System.currentTimeMillis();

// 并行，默认使用CPU * 2个线程
//        longList.stream().parallel().forEach(
//                i -> {
//                    try {
//                        blockingQueue.put(i);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
                longList.stream().forEach(
                i -> {
                    try {
                        blockingQueue.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println("blockingQueue:" + blockingQueue.toString());

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        System.out.println("longList:"+longList.toString());

//        BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue(10000);
    }

}
