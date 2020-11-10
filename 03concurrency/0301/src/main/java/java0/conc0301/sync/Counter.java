package java0.conc0301.sync;

public class Counter {
    
    public final static int A=10;
    
    public static int B=10;
    
    private volatile int sum = 0;
    public synchronized void incr() {
        sum=sum+1;
    }
    public int getSum() {
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException {
        int loop = 100000;
        
        // test single thread
        Counter counter = new Counter();
        for (int i = 0; i < loop; i++) {
            counter.incr();
        }
        System.out.println("single thread: " + counter.getSum());
    
        // test multiple threads
        final Counter counter2 = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(1000);
//        while (Thread.activeCount()>2){//当前线程的线程组中的数量>2
//            Thread.yield();
//        }
        System.out.println("multiple threads: " + counter2.getSum());
    
    
    }
}
