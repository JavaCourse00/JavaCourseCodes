package io.github.kimmking.gateway.outbound.okhttp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

    private String backendUrl;
    private OkHttpClient client;

    public OkhttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length()-1) : backendUrl;
        this.client = new OkHttpClient();
    }

    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        Request request = buildRequest(fullRequest);

        FullHttpResponse httpResponse = null;
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();

            httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body.bytes()));
            httpResponse.headers().set("Content-Type", "application/json");
            httpResponse.headers().setInt("Content-Length", (int) body.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
            httpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            ctx.close();
        } finally {
            if (!HttpUtil.isKeepAlive(fullRequest)) {
                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
            } else {
                ctx.write(httpResponse);
            }
            ctx.flush();
        }
    }

    @NotNull
    private Request buildRequest(FullHttpRequest fullRequest) {
        String url = this.backendUrl + fullRequest.uri();
        Request.Builder builder = new Request.Builder().url(url);
        enhanceRequest(builder, fullRequest);
        return builder.build();
    }

    /**
     * 请求头处理
     * @param builder
     * @param fullHttpRequest
     */
    protected void enhanceRequest(Request.Builder builder, FullHttpRequest fullHttpRequest) {

    }

}
