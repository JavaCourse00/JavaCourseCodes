package java0.conc0303.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {
    
    public static void main(String[] args) {
    
        // ArrayList，LinkedList，Vector不安全，运行报错
        // why Vector 也不安全
//        List<Integer> list = new ArrayList<Integer>();
//        List<Integer> list = new LinkedList<>();
//        List<Integer> list = new Vector<>();
    
        // 只有CopyOnWriteArrayList 安全，不报错
        List<Integer> list = new CopyOnWriteArrayList();
        
        for (int i = 0; i < 10000; i++)
        {
            list.add(i);
        }
    
        T1 t1 = new T1(list);
        T2 t2 = new T2(list);
        t1.start();
        t2.start();
        
    }
    
    public static class T1 extends Thread
    {
        private List<Integer> list;
        
        public T1(List<Integer> list)
        {
            this.list = list;
        }
        
        public void run()
        {
            for (Integer i : list)
            {
            }
        }
    }
    
    public static class T2 extends Thread
    {
        private List<Integer> list;
        
        public T2(List<Integer> list)
        {
            this.list = list;
        }
        
        public void run()
        {
            for (int i = 0; i < list.size(); i++)
            {
                list.remove(i);
            }
        }
    }
    
}
