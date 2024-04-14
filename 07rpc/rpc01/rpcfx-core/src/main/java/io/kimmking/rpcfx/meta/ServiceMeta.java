package io.kimmking.rpcfx.meta;

import lombok.Builder;
import lombok.Data;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 19:46
 */
@Data
@Builder
public class ServiceMeta {

    private String app;
    private String namespace;
    private String env;
    private String name;

    @Override
    public String toString() {
        return String.format("%s_%s_%s_%s", app, namespace, env, name);
    }
}