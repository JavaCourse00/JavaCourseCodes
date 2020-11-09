package java0.conc0303.tool;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo2 {
    
    private final static int threadCount = 200;
    
    public static void main(String[] args) throws Exception {
        
        ExecutorService exec = Executors.newCachedThreadPool();
        
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    test(threadNum);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("==>所有程序员完成任务，项目顺利上线！");
        exec.shutdown();
    }
    
    private static void test(int threadNum) throws Exception {
        Thread.sleep(100);
        System.out.println(String.format("程序员[%d]完成任务。。。", threadNum));
        Thread.sleep(100);
    }
}