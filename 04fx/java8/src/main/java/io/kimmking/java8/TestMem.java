package io.kimmking.java8;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class TestMem {
    public static void main(String[] args) {
        int[] arr1 = new int[256];
        int[][] arr2 = new int[128][2];
        int[][][] arr3 = new int[64][2][2];

        print("1-size : " + GraphLayout.parseInstance(arr1).totalSize());
        print(ClassLayout.parseInstance(arr1).toPrintable());
        print("2-size : " + GraphLayout.parseInstance(arr2).totalSize());
        print(ClassLayout.parseInstance(arr2).toPrintable());
        print(GraphLayout.parseInstance(arr2).toPrintable());
        print("3-size : " + GraphLayout.parseInstance(arr3).totalSize());
        print(ClassLayout.parseInstance(arr3).toPrintable());
        print(GraphLayout.parseInstance(arr3).toPrintable());
        System.out.println();
    }

    static void print(String message) {
        System.out.println(message);
        System.out.println("-------------------------");
    }
}