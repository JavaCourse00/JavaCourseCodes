package io.kimmking.spring01;

import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class LombokDemo {
    
    public static void main(String[] args) throws IOException {
        
        new LombokDemo().demo();
        
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("KK01");
        System.out.println(student1.toString());
        
        Student student2 = Student.create();
        System.out.println(student2.toString());
    }
    
    private void demo() {
       log.info("demo in log."); 
    }
    
}
