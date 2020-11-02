package java0.conc0301;

public class ThreadMain2 {

    public static void main(String[] args) {

        ThreadB threadB = new ThreadB();
        for (int i = 0; i < 5; i++) {
            new Thread(threadB, "线程名称：（" + i + "）").start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //返回对当前正在执行的线程对象的引用
        Thread threadMain = Thread.currentThread();
        System.out.println("这是主线程：");
        System.out.println("返回当前线程组中活动线程的数目：" + Thread.activeCount());
        System.out.println("主线程的名称：" + threadMain.getName());
        System.out.println("返回该线程的标识符：" + threadMain.getId());
        System.out.println("返回线程的优先级：" + threadMain.getPriority());
        System.out.println("返回线程的状态：" + threadMain.getState());
        System.out.println("返回该线程所属的线程组：" + threadMain.getThreadGroup());
        System.out.println("测试线程是否为守护线程：" + threadMain.isDaemon());


//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }


}
