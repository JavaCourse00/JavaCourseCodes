package java0.conc0301;

public class ThreadCount {
    public static void main(String[] args) throws InterruptedException {
        //System.out.println("system："+Thread.currentThread().getThreadGroup().getParent());
        Thread.currentThread().getThreadGroup().getParent().list();

//        System.out.println("main："+Thread.currentThread().getThreadGroup());
//        Thread.currentThread().getThreadGroup().list();
    }
}
