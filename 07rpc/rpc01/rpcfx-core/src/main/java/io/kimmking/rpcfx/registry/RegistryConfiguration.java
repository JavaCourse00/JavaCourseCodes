package io.kimmking.rpcfx.registry;

import io.kimmking.rpcfx.registry.kkregistry.KKRegistryCenter;
import io.kimmking.rpcfx.registry.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/9 01:05
 */

@Configuration
public class RegistryConfiguration {

    @Bean
    RegistryCenter createRC() {
        return new ZookeeperRegistryCenter(); //KKRegistryCenter();
    }

}
