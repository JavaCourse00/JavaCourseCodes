package io.kimmking.rpcfx.utils;

import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUtils {

    public static String methodSign(Method method) {
        if (method != null) {
            StringBuilder builder = new StringBuilder("method:");
            String name = method.getName();
            builder.append(name);
            builder.append("_");
            int count = method.getParameterCount();
            builder.append(count);
            builder.append("_");
            if (count > 0) {
                Class<?>[] classes = method.getParameterTypes();
                Arrays.stream(classes).forEach(c -> builder.append(c.getName() + ","));
            }
            String string = builder.toString();
            return DigestUtils.md5DigestAsHex(string.getBytes());
        }
        return "";
    }

}