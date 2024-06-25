package io.github.kimmking.gateway.filter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("kk", "java-1-nio");
        response.setStatus(HttpResponseStatus.CREATED);
//        byte[] array = response.content().array();
//        String content = new String(array);
//        System.out.println(content);
//        content = content + ",kimmking";
        byte[] bytes = "hello,kimm.".getBytes();
        //response.headers().setInt("Content-Length", bytes.length);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        ByteBuf content = response.content();
        content.clear();
        content.writeBytes(byteBuf);
    }
}
