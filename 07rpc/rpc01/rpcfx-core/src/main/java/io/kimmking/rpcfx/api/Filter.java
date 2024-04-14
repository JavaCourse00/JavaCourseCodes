package io.kimmking.rpcfx.api;

public interface Filter {

    RpcfxResponse prefilter(RpcfxRequest request);

    RpcfxResponse postfilter(RpcfxRequest request, RpcfxResponse response);

    // Filter next();

}
