package io.github.kimmking.gateway.outbound.okhttp;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import okhttp3.Request;

import java.util.Iterator;
import java.util.Map;

public class OkhttpOutboundFilterHandler extends OkhttpOutboundHandler {

    public OkhttpOutboundFilterHandler(String backendUrl) {
        super(backendUrl);
    }

    @Override
    protected void enhanceRequest(Request.Builder builder, FullHttpRequest fullHttpRequest) {
        HttpHeaders headers = fullHttpRequest.headers();
        Iterator<Map.Entry<String, String>> iterator = headers.iteratorAsString();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.header(next.getKey(), next.getValue());
        }
    }

}

