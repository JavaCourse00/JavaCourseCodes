package io.kimmking.rpcfx.api;

import io.kimmking.rpcfx.meta.InstanceMeta;

import java.util.List;

public interface LoadBalancer {

    InstanceMeta select(List<InstanceMeta> instances);

}
