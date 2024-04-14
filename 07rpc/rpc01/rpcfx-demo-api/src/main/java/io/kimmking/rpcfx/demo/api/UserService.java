package io.kimmking.rpcfx.demo.api;

public interface UserService {

    User findById(int id);

    User find(int timeout);

    //User findById(int id, String name);

}
