package java0.conc0303.collection;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo2 {
    
    private final static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
    
    public static void main(String[] args) {
        // 这个例子再次证明，
        // 多个步骤的操作，不能保证原子性
        // list.size() 获取到的数，再继续用list时，可能已经变了
        // 
        test();
    }
    public static void test(){
        for(int i = 0; i<10000; i++){
            list.add("string" + i);
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (list.size() > 0) {    // todo ： 下一个get操作执行时，size可能已经是0了
                        String content = list.get(list.size() - 1);
                    }else {
                        break;
                    }
                }
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(list.size() <= 0){
                        break;
                    }
                    list.remove(0);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
