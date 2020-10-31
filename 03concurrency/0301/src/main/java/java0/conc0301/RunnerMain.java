
package java0.conc0301;

public class RunnerMain {

    public static void main(String[] args) {

        Runner1 runner1 = new Runner1();
        Thread thread1 = new Thread(runner1);

        Runner2 runner2 = new Runner2();
        Thread thread2 = new Thread(runner2);

        thread1.start();
        thread2.start();

        thread2.interrupt();

        System.out.println(Thread.activeCount());
        
//        Thread.currentThread().getThreadGroup().list();
//        System.out.println(Thread.currentThread().getThreadGroup().getParent().activeGroupCount());
//        Thread.currentThread().getThreadGroup().getParent().list();
    }
}
