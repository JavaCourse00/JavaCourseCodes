package io.kimmking.spring04;

import org.springframework.core.Ordered;

public interface ABCPlugin extends Ordered {

    void startup()  throws Exception;
    void shutdown() throws Exception;

}
