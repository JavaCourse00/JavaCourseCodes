package java0.conc0303.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CopyOnWriteArrayListDemo1 {
    private static final int THREAD_POOL_MAX_NUM = 10;
    //private List<String> mList = new ArrayList<String>();  // ArrayList 无法运行
    private List<String> mList = new CopyOnWriteArrayList<>();
    
    public static void main(String args[]) {
        new CopyOnWriteArrayListDemo1().start();
    }
    
    private void initData() {
        for (int i = 0; i <= THREAD_POOL_MAX_NUM; i++) {
            this.mList.add("...... Line " + (i + 1) + " ......");
        }
    }
    
    private void start() {
        initData();
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_MAX_NUM);
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            service.execute(new ListReader(this.mList));
            service.execute(new ListWriter(this.mList, i));
        }
        service.shutdown();
    }
    
    private class ListReader implements Runnable {
        private List<String> mList;
        
        public ListReader(List<String> list) {
            this.mList = list;
        }
        
        @Override
        public void run() {
            if (this.mList != null) {
                for (String str : this.mList) {
                    System.out.println(Thread.currentThread().getName() + " : " + str);
                }
            }
        }
    }
    
    private class ListWriter implements Runnable {
        private List<String> mList;
        private int mIndex;
        
        public ListWriter(List<String> list, int index) {
            this.mList = list;
            this.mIndex = index;
        }
        
        @Override
        public void run() {
            if (this.mList != null) {
                //this.mList.remove(this.mIndex);
                this.mList.add("...... add " + mIndex + " ......");
            }
        }
    }
}