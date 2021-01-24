package java0.conc0301.op;

public class Join {
    
    public static void main(String[] args) {
        Object oo = new Object();
    
        MyThread thread1 = new MyThread("thread1 -- ");
        //oo = thread1;
        thread1.setOo(oo);
        thread1.start();
        
        synchronized (oo) {  // 这里用oo或thread1/this
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    try {
                        oo.wait(0);
                        //thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -- " + i);
            }
        }
    }
    
}

class MyThread extends Thread {
    
    private String name;
    private Object oo;
    
    public void setOo(Object oo) {
        this.oo = oo;
    }
    
    public MyThread(String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        synchronized (oo) { // 这里用oo或this，效果不同
            for (int i = 0; i < 100; i++) {
                System.out.println(name + i);
            }
        }
    }
    
}