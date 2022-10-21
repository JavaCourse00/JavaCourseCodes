package io.kimmking.spring04;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component()
public class MockABCPlugin2 implements ABCPlugin {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void startup() throws Exception {
        System.out.println(this.toString()+" started.");
    }

    @Override
    public void shutdown() throws Exception {
        System.out.println(this.toString()+" stopped.");
    }
}
