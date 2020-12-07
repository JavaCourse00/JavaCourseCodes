package java0.conc0302.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExceptionDemo {
    
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
    
        try {
            Future<Double> future = executorService.submit(() -> {
                int a = 1;
                return 10.0/(a-1);
            });
    
            double b = future.get();
            System.out.println(b);
            
        } catch (Exception ex) {
            System.out.println("catch execute");
            ex.printStackTrace();
        }
        
        try {
            executorService.execute(() -> {
                    int a = 1;
                    float b = 10/(a-1);
                });
        } catch (Exception ex) {
            System.out.println("catch execute");
            ex.printStackTrace();
        }
        
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }
    
}
