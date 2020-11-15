package io.kimmking.springjms;

import io.kimmking.spring01.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class JmsReceiver {
    
    public static void main( String[] args ) throws IOException {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:springjms-receiver.xml");
        
        System.in.read();
        
        System.out.println("send successfully, please visit http://localhost:8161/admin to see it");
    }
    
}
