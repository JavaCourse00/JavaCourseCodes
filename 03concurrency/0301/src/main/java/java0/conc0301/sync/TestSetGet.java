package java0.conc0301.sync;

import java.lang.reflect.Field;

public class TestSetGet {

    public static void main(String[] args) throws Exception {

        final SetGet s = new SetGet();
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    s.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        long start = System.currentTimeMillis();
        s.set(10);
        System.out.println(" ... " + ( System.currentTimeMillis() - start ));

    }

    public static class SetGet {

        int a = 0;
        public synchronized void set(int v) throws Exception {
            System.out.println(addr(Thread.currentThread()));
            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getName() +" setting " +v);
            Thread.sleep(100000);
            a = v;
            System.out.println(Thread.currentThread().getName() +" set " +v);
        }

        private Object addr(Thread currentThread) throws Exception {
            Field f = Thread.class.getDeclaredField("eetop");
            f.setAccessible(true);
            Long addr = (Long)f.get(currentThread);
            return Long.toHexString(addr);
        }

        public synchronized int get() throws Exception {
            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getName() +" getting ");
            Thread.sleep(100000);
            System.out.println(Thread.currentThread().getName() + " get ");
            return a;
        }

    }
}
