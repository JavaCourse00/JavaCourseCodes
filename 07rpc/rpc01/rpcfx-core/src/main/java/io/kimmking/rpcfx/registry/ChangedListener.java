package io.kimmking.rpcfx.registry;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 20:19
 */
public interface ChangedListener<T> {

    void fireEvent(Event<T> e);

}
