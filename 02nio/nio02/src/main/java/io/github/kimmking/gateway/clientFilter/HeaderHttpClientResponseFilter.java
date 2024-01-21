package io.github.kimmking.gateway.clientFilter;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpClientResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("test", "test-nio");
    }
}
