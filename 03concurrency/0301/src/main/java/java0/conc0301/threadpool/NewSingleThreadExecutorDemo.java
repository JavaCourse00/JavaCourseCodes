
package java0.conc0301.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewSingleThreadExecutorDemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            final int no = i;
            executorService.execute(() -> {
                System.out.println("start:" + no);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end:" + no);
            });
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
