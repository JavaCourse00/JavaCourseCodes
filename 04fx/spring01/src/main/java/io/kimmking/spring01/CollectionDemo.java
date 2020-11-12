package io.kimmking.spring01;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionDemo {
    public static void main(String[] args) throws IOException {
    
        List<Integer> list = Arrays.asList(4,2,3,5,1,2,2,7,6);   // Arrays还可以包装stream
        print(list);
        Collections.sort(list);
        print(list);
        Collections.reverse(list);
        print(list);
        Collections.shuffle(list);
        print(list);
    
        System.out.println(Collections.frequency(list, 2));
        System.out.println(Collections.max(list));
                
        Collections.fill(list,8);
        print(list);
        
        list = Collections.singletonList(6);
        print(list);
        
    }
    
    private static void print(List<Integer> list) {
        System.out.println(String.join(",",list.stream().map(i -> i.toString()).collect(Collectors.toList()).toArray(new String[]{})));  
    }
    
}
