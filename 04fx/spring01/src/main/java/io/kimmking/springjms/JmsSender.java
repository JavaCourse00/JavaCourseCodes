package io.kimmking.springjms;

import io.kimmking.spring01.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsSender {
    
    public static void main( String[] args )
    {
        Student student2 = Student.create();
        
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:springjms-sender.xml");
        
        SendService sendService = (SendService)context.getBean("sendService");

        student2.setName("KK103");

        sendService.send(student2);
        
        System.out.println("send successfully, please visit http://localhost:8161/admin to see it");
    }
    
}
