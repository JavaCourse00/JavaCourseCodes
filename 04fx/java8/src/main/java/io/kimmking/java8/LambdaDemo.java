package io.kimmking.java8;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class LambdaDemo<T extends Serializable&Comparable&Collection> {
    
    public static void main(String args[]){
        LambdaDemo demo = new LambdaDemo();
    
        MathOperation op = new MathOperation<Integer>() {
            @Override
            public Integer operation(int a, int b) {
                return 1;
            }
        };
        
        MathOperation op1 = (a, b) -> 1;
        
        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;
    
                // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b + 1.0;
        
        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> { 
            int c = 1000;
            return a * b + c; 
        };
        
        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;
        
        System.out.println("10 + 5 = " + demo.operate(10, 5, addition));
        System.out.println("10 - 5 = " + demo.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + demo.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + demo.operate(10, 5, division));
    
        //System.out.println("10 ^ 5 = " + demo.operate(10, 5, (a, b) -> new Double(Math.pow(a,b)).intValue()));
    
        System.out.println("10 ^ 5 = " + demo.operate(10, 5, (a, b) -> Math.pow(a,b)));
        
        // 不用括号
        GreetingService greetService1 = message ->
                System.out.println("Hello " + message);
        
        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);
    
        GreetingService greetService3 = System.out::println;
    
        Arrays.asList(1,2,3).forEach( x -> System.out.println(x+3));
        Arrays.asList(1,2,3).forEach( LambdaDemo::println );
        
        greetService1.sayMessage("kimmking");
        greetService2.sayMessage("Java");
    }
    
    private static void println(int x) {
        System.out.println(x+3);
    }
    
    interface MathOperation<T> {
        T operation(int a, int b); // 返回类型+函数名+参数类型的列表
    }
    
    interface GreetingService {
        void sayMessage(String message);
    }
    
    private <T> T operate(int a, int b, MathOperation<T> mathOperation){
        return mathOperation.operation(a, b);
    }
    
}
