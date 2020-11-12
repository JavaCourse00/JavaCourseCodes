package io.kimmking.spring01;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamDemo {
    
    public static void main(String[] args) throws IOException {
    
        List<Integer> list = Arrays.asList(4,2,3,5,1,2,2,7,6);
        print(list);
        
        // Optional
        Optional<Integer> first = list.stream().findFirst();
        
        System.out.println(first.map(i -> i * 100).orElse(100));
        
        
        
        int sum = list.stream().filter( i -> i<4).distinct().reduce(0,(a,b)->a+b);
        System.out.println("sum="+sum);
        
        //Map map = list.stream().collect(Collectors.toMap(a->a,a->(a+1)));
        Map<Integer,Integer> map = list.parallelStream().collect(Collectors.toMap(a->a,a->(a+1),(a,b)->a, HashMap::new));
        print(map);
        
        
        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        List<Integer> list1 = map.entrySet().parallelStream().map(e -> e.getKey()+e.getValue()).collect(Collectors.toList());
        print(list1);
    
        // parallelStream()
        
        // 总结：
        // 1. Fluent API：继续Stream
        // 2. 终止操作：得到结果
        
    
    }
    
    
    private static void print(Object obj) {
        System.out.println(JSON.toJSONString(obj));
    }
}
