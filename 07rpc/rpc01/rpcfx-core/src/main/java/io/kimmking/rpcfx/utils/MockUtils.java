package io.kimmking.rpcfx.utils;

import lombok.Data;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/11 03:15
 */
public class MockUtils {

    public static Object mock(Class<?> clazz, Type[] generics) {
        boolean primitiveOrWrapper = ClassUtils.isPrimitiveOrWrapper(clazz);
        if(primitiveOrWrapper) return mockPrimitive(clazz);
        if(String.class.equals(clazz)) return mockString();
        if (Number.class.isAssignableFrom(clazz)) {
            return 10;
        }
        if(clazz.isArray()) {
            return mockArray(clazz.getComponentType());
        }
        if(List.class.isAssignableFrom(clazz)) {
            return mockList(clazz, generics[0]);
        }
        if(Map.class.isAssignableFrom(clazz)) {
            return mockMap(clazz, generics[1]);
        }
        return mockPojo(clazz);
    }

    private static Object mockMap(Class<?> clazz, Type generic) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("a", mock((Class)generic, null));
        map.put("b", mock((Class)generic, null));
        return map;
    }

    private static Object mockList(Class<?> clazz, Type generic) {
        List list = new ArrayList<>();
        list.add(mock((Class)generic, null));
        list.add(mock((Class)generic, null));
        return list;
    }

    private static Object mockArray(Class<?> clazz) {
        Object array = Array.newInstance(clazz, 2);
        Array.set(array, 0, mock(clazz, null));
        Array.set(array, 1, mock(clazz,null));
        return array;
    }

    private static Object mockPojo(Class<?> clazz) {
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Type genericType = f.getGenericType();
//                System.out.println(f.getGenericType());
//                System.out.println(f.getType());
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    System.out.println("genericType="+Arrays.toString(typeArguments));
                    f.set(object, mock(f.getType(), typeArguments));
                } else {
                    f.set(object, mock(f.getType(),null));
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object mockString() {
        return "this_is_a_mock_string";
    }

    private static Object mockPrimitive(Class<?> clazz) {

        if (Boolean.class.equals(clazz)) {
            return true;
        }

        return 1;
    }

    public static void main(String[] args) {

        System.out.println(mock(ListPojo.class,null));


//        System.out.println(mock(Byte.class));
//        System.out.println(mock(Character.class));
//        System.out.println(mock(Boolean.class));
//        System.out.println(mock(Integer.class));
//        System.out.println(mock(Float.class));
//        System.out.println(mock(Short.class));
//        System.out.println(mock(Long.class));
//        System.out.println(mock(Double.class));
//        System.out.println(mock(BigInteger.class));
//        System.out.println(mock(BigDecimal.class));
//        System.out.println(mock(String.class));

//        System.out.println(mock(Pojo.class));

//        Arrays.stream(((Pojo[]) mock(new Pojo[]{}.getClass()))).forEach(System.out::println);

    }


    @Data
    public static class Pojo {
        private int id;
        private String name;
        private float amount;
        private InnerPojo inner;
    }

    @Data
    public static class InnerPojo {
        private int value;
        private String key;
    }

    @Data
    public static class ListPojo {
        private List<InnerPojo> list;
        private Integer inner;
        private Map<String, InnerPojo> map;
    }

}
