package com.example.demo;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        ClassLoader app = this.getClass().getClassLoader();
//        System.out.println(" APP Classloader => " + app.getName());
//        for (Package definedPackage : app.getDefinedPackages()) {
//            System.out.println(definedPackage.getName());
//        }


        printObject();
        printString();
        printIntArray();

        printUser();

    }

    private void printUser() {
        User user = new User(true, (byte) 65,12345, 8888, 12345678L, 9999999L, "a");
        System.out.println(ClassLayout.parseInstance(user).toPrintable());

        System.out.println(GraphLayout.parseInstance(user).toPrintable());
        System.out.println(GraphLayout.parseInstance(user).toFootprint());

//        try {
//            GraphLayout.parseInstance(user).toImage("user.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void printObject() {
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
    }

    private void printString() {
        System.out.println(ClassLayout.parseInstance("abcde12345").toPrintable());
    }

    private void printIntArray() {
        System.out.println(ClassLayout.parseInstance(new int[2]).toPrintable());
        System.out.println(ClassLayout.parseInstance(new int[12]).toPrintable());
        System.out.println(ClassLayout.parseInstance(new int[2][50]).toPrintable());
        System.out.println(ClassLayout.parseInstance(new int[50][2]).toPrintable());
    }

    public static class User {
        public User(boolean b1, byte b2, int a, Integer b, long c, Long d, String s) {
            this.b1 = b1;
            this.b2 = b2;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.s = s;
        }

        private boolean b1;
        private byte b2;
        private int a;
        private Integer b;
        private long c;
        private Long d;
        private String s;
    }

}
