package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestHeaderFilter;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundFilterHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundFilterHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;
    private OkhttpOutboundHandler handler;
    private HttpRequestFilter filter;

    public HttpInboundFilterHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new OkhttpOutboundFilterHandler(this.proxyServer);
        filter = new HttpRequestHeaderFilter();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            filter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
