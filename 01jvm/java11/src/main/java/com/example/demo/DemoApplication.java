package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ClassLoader app = this.getClass().getClassLoader();
        System.out.println(" APP Classloader => " + app.getName());
        for (Package definedPackage : app.getDefinedPackages()) {
            System.out.println(definedPackage.getName());
        }


    }
}
