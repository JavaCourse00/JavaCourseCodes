package io.kimmking.anno;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class AnnoDemo {

    public static void main(String[] args) {
        IA2 ia2 = new IA2() {
            @Override
            public void a2() {
                System.out.println("a2.");
            }

            @Override
            public void a1() {
                System.out.println("a1.");
            }
        };

        Annotation[] annotations = ia2.getClass().getInterfaces()[0].getAnnotations();
        Arrays.stream(annotations).forEach(x -> System.out.println(x.annotationType().getCanonicalName()));

        Annotation[] annos2 = ia2.getClass().getInterfaces()[0].getInterfaces()[0].getAnnotations();
        Arrays.stream(annos2).forEach(x -> System.out.println(x.annotationType().getCanonicalName()));
        Arrays.stream(annos2).forEach(
                x -> {
                    IAnno2 anno2 = (IAnno2) x;
                    System.out.println(anno2.value());
                });

    }

}
