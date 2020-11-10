package java0.conc0303.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SyncListDemo {
    
    public static void main(String[] args) {
        List list0 = Arrays.asList(1,2,3,4,5,6,7,8,8);
        list0.set(8,9);        // 可以修改内容，不能变动元素数量
        // list0.add(10)  will throw an error
        
        List list = new ArrayList(); // 正常List，可以操作
        list.addAll(list0);
        
        List list1 = Collections.synchronizedList(list);
        
        // 多线程操作
        // to do something
    
        System.out.println(Arrays.toString(list1.toArray()));
    
        Collections.shuffle(list1);
    
        
    
        System.out.println(Arrays.toString(list1.toArray()));
        
        
        // 假如不再修改
        
        List list2 = Collections.unmodifiableList(list1);
    
        System.out.println(list2.getClass());
    
        list2.set(8,10);
    
        System.out.println(Arrays.toString(list2.toArray()));
        
        list2.add(11);
    
        System.out.println(Arrays.toString(list2.toArray()));
    }
    
}
