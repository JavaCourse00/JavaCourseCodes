package com.example.demo;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;

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

        printListGraph();
    }

    private void printListGraph() {

        Random random = new Random();
        List<Integer> list = new ArrayList<>(128);
        for (int i = 0; i < 128; i++) {
            list.add(i, random.nextInt(128));
        }

        try {
            System.out.println(" ===> GraphLayout.parseInstance(list).toImage(\"list.png\")");
            GraphLayout.parseInstance(list).toImage("list.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> map = new HashMap(256);
        for (int i = 0; i < 128; i++) {
            map.put("K" + i, i);
        }

        try {
            System.out.println(" ===> GraphLayout.parseInstance(map).toImage(\"map.png\"))");
            GraphLayout.parseInstance(map).toImage("map.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void printUser() {

        System.out.println(" ===> ClassLayout.parseInstance(user).toPrintable()");
        User user = new User(true, (byte) 65,12345, 8888, 12345678L, 9999999L, "a");
        System.out.println(ClassLayout.parseInstance(user).toPrintable());

        System.out.println(" ===> GraphLayout.parseInstance(user).toPrintable()");
        System.out.println(GraphLayout.parseInstance(user).toPrintable());

        System.out.println(" ===> GraphLayout.parseInstance(user).toFootprint()");
        System.out.println(GraphLayout.parseInstance(user).toFootprint());

//        try {
//            GraphLayout.parseInstance(user).toImage("user.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        User user1 = new User(false, (byte) 66,12346, 5555, 12345679L, 888888L, "b");
        Klass klass = new Klass("Klass1", Arrays.asList(user,user1));

        System.out.println(" ===> ClassLayout.parseInstance(klass).toPrintable()");
        System.out.println(ClassLayout.parseInstance(klass).toPrintable());

        System.out.println(" ===> GraphLayout.parseInstance(klass).toImage(\"klass.png\")");
        try {
            GraphLayout.parseInstance(klass,user,user1).toImage("klass.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public static class Klass {
        private String klassName;
        private List<User> users;

        public Klass(String klassName, List<User> users) {
            this.klassName = klassName;
            this.users = users;
        }
    }

}
