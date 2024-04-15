package io.kimmking.rpcfx.registry;

import io.kimmking.rpcfx.meta.InstanceMeta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 20:20
 */
public interface Event<T> {

    T getData();

    static Event<List<InstanceMeta>> withData(List<InstanceMeta> list) {
        return new ChangedEvent(list);
    }

    @Data
    @AllArgsConstructor
    class ChangedEvent implements Event<List<InstanceMeta>> {
        List<InstanceMeta> data;
    }

}
