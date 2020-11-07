package java0.conc0303.future;

import java.util.Random;
import java.util.concurrent.*;

public class FutureDemo1 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
//                return new Random().nextInt();
                int numberInt = new Random().nextInt();
                System.out.println(numberInt);
                return numberInt;

            }
        });
        executor.shutdown();
        // try 超时处理
        try {
            System.out.println("result:" + result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}