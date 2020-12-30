package io.kimmking.cache.cluster;

import lombok.SneakyThrows;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public final class ClusterJedis {

    private static JedisCluster CLUSTER = createJedisCluster();

    private static JedisCluster createJedisCluster() {
        JedisCluster jedisCluster = null;
                // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("127.0.0.1", 6379));
        hostAndPortsSet.add(new HostAndPort("127.0.0.1", 6380));
        // hostAndPortsSet.add(new HostAndPort("127.0.0.1", 6381));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(12);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(16);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(4);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        jedisCluster = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
        return jedisCluster;
    }

    public static JedisCluster getJedisCluster() {
        return CLUSTER;
    }

    @SneakyThrows
    public static void close(){
        CLUSTER.close();
    }
}
