package io.github.kimmking.gateway.clientFilter;

import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpClientResponseFilter {

    void filter(FullHttpResponse response);

}
