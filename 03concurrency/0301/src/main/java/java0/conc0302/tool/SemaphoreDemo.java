
package java0.conc0302.tool;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        // 启动线程
        for (int i = 0; i <= 10; i++) {
            // 生产者
            new Thread(new Producer()).start();
            // 消费者
            new Thread(new Consumer()).start();
        }
    }


    // 仓库
    static Warehouse buffer = new Warehouse();

    static class Producer implements Runnable {

        static int num = 1;

        @Override
        public void run() {
            int n = num++;
            while (true) {
                try {
                    buffer.put(n);
                    System.out.println(">" + n);
                    // 速度较快。休息10毫秒
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Consumer implements Runnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("<" + buffer.take());
                    // 速度较慢，休息1000毫秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Warehouse {
        // 非满锁
        final Semaphore notFull = new Semaphore(10);
        // 非空锁
        final Semaphore notEmpty = new Semaphore(0);
        // 核心锁
        final Semaphore mutex = new Semaphore(1);
        // 库存容量
        final Object[] items = new Object[10];
        int putptr, takeptr, count;


        /**
         * 放库存
         *
         * @param obj
         * @throws InterruptedException
         */
        public void put(Object obj) throws InterruptedException {
            notFull.acquire();
            mutex.acquire();
            items[putptr] = obj;
            try {
                if (++putptr == items.length) {
                    putptr = 0;
                    ++count;
                }
            } finally {
                mutex.release();
                notEmpty.release();
            }
        }

        /**
         * 取库存
         *
         * @return
         * @throws InterruptedException
         */
        public Object take() throws InterruptedException {
            notEmpty.acquire();
            mutex.acquire();
            Object obj = items[takeptr];
            try {
                if (++takeptr == items.length) {
                    takeptr = 0;
                    --count;
                }
                return obj;
            } finally {
                mutex.release();
                notFull.release();
            }
        }
    }

}
