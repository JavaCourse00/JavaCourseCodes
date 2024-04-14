package io.kimmking.rpcfx.api;

import io.kimmking.rpcfx.meta.InstanceMeta;

import java.util.List;

public interface Router {

    List<InstanceMeta> route(List<InstanceMeta> instances);
}
