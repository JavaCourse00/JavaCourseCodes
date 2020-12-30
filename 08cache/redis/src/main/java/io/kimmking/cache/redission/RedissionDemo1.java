package io.kimmking.cache.redission;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissionDemo1 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6380");
        //config.useSingleServer().setPassword("");

        final RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("lock1");

        try{
            lock.lock();

            RMap<String, String> rmap = client.getMap("map1");

            for (int i = 0; i < 15; i++) {
                rmap.put("rkey:"+i, "rvalue:1-"+i);
            }

            System.out.println(rmap.get("rkey:10"));

        }finally{
            lock.unlock();
        }
    }

}
