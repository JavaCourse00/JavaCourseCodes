package io.github.kimmking.gateway.inbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.internal.PlatformDependent;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/31 下午7:13
 */
public class NettyInfoHandler {

    public final static NettyInfoHandler INSTANCE = new NettyInfoHandler();

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        System.out.println("NettyInfoHandler.handle...");
        Map<String, String> infos = new HashMap<>();
        infos.put("netty.usedDirectMemory", ""+getNettyUsedDirectMemory());
        infos.put("netty.directMemoryLimit", ""+getNettyDirectMemoryLimit());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        infos.forEach((k, v) -> {
            sb.append("\"").append(k).append("\"")
                    .append(":")
                    .append("\"").append(v).append("\"").append(",");
        });
        if(sb.length()>1) {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("}");

        byte[] body = ("{\"code\":200,\"msg\":\"success\",\"data\":" + sb +"}").getBytes();
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));

        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", body.length);
        response.headers().set("kk.gw.hanlder", "netty.info");

        if (fullRequest != null) {
            if (!HttpUtil.isKeepAlive(fullRequest)) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                //response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
        ctx.flush();
        //ctx.close();

    }

    @SneakyThrows
    private static long getNettyUsedDirectMemory() {
        Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
        field.setAccessible(true);
        AtomicLong o = (AtomicLong)field.get(null);
        return o.get();
    }

    @SneakyThrows
    private static long getNettyDirectMemoryLimit() {
        Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_LIMIT");
        field.setAccessible(true);
        return (Long)field.get(null);
    }

}
