package io.kimmking.java8;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

    static String file = "conf/a.properties";

    public static void main(String[] args) {

        // testClassLoaderRootPath(); // classloader方式不能加根路径，否则文件路径会直接被替换掉最终成了 /conf/a.properties
        testClassLoader(); // 从当前的classpath，不管是 文件夹，还是jar里去找 资源
        testClassRootPath(); // 判断第一个字符是根路径，去掉根，转成 testClassLoader 调用
        // testClass(); // class 方式必须加根路径，不加根会根据类路径io.kimmking.java8.ResourceLoader 去拼成 io/kimmking/java8/conf/a.properties


    }

    private static void testClassLoader() {
        System.out.println("====> testClassLoader");
        loadStream(ResourceLoader.class.getClassLoader().getResourceAsStream(file));
    }

    private static void testClassLoaderRootPath() {
        System.out.println("====> testClassLoader");
        loadStream(ResourceLoader.class.getClassLoader().getResourceAsStream("/"+file));
    }

    private static void testClass() {
        System.out.println("====> testClass");
        loadStream(ResourceLoader.class.getResourceAsStream(file));
    }

    private static void testClassRootPath() {
        System.out.println("====> testClassRootPath");
        loadStream(ResourceLoader.class.getResourceAsStream("/"+file));
    }

    @SneakyThrows
    private static void loadStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        System.out.println(buffer.toString());
        reader.close();
        in.close();
    }

}
