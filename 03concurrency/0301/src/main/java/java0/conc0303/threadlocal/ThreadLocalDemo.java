package java0.conc0303.threadlocal;

public class ThreadLocalDemo {
    
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };
    
    public ThreadLocal<Integer> getThreadLocal() {
        return seqNum;
    }
    
    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }
    
    
    public static void main(String[] args) {
        ThreadLocalDemo threadLocalMain = new ThreadLocalDemo();
    
        SnThread client1 = new SnThread(threadLocalMain);
        SnThread client2 = new SnThread(threadLocalMain);
        SnThread client3 = new SnThread(threadLocalMain);
        
        client1.start();
        client2.start();
        client3.start();
    }
    
    
    private static class SnThread extends Thread {
        private ThreadLocalDemo sn;
        
        public SnThread(ThreadLocalDemo sn) {
            this.sn = sn;
        }
        
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("thread[" + Thread.currentThread().getName() + "] ---> sn [" + sn.getNextNum() + "]");
            }
            sn.getThreadLocal().remove();
        }
    }
}
