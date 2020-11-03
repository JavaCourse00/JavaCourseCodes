package io.github.kimmking.gateway.outbound.myselfhttpclient;

import io.github.kimmking.gateway.ThreadPool;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MyHttpOutboundHandler {

    private static CloseableHttpClient client;
    private String backendUrl;
    private ThreadPoolExecutor proxyService;
    public MyHttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        client = HttpClients.createDefault();
        proxyService = ThreadPool.getThreadPoolExecutor();
    }

    public void handler(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullRequest.uri();
        if (StringUtils.isEmpty(url)) {
            return;
        }
        // 创建httpGet请求对象
        final HttpGet httpGet = new HttpGet(url);
        // 设置时间等参数
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000) // 链接超时时间
                .setConnectionRequestTimeout(1000) // 连接请求超时时间
                .setSocketTimeout(1000) // 套接字超时时间
                .build();
        // HttpHeaders httpHeaders =   fullRequest.headers();
        // List<Map.Entry<String, String>> headerList =  httpHeaders.entries();
        // 设置全部请求头到对后端调用的请求头中
        // headerList.forEach(header -> {
        //     Map.Entry<String, String> map = header;
        //     httpGet.addHeader(map.getKey(), map.getValue());
        // });
        httpGet.setConfig(config);
        proxyService.submit(() -> doGet(httpGet, fullRequest, ctx));
    }

    private void doGet(HttpGet httpGet,FullHttpRequest fullRequest,ChannelHandlerContext ctx){
        try {
            // 执行请求
            client.execute(httpGet, new ResponseHandler<HttpResponse>() {
                @Override
                public HttpResponse handleResponse(HttpResponse httpResponse) {
                    try {
                        // 返回数据到浏览器
                        doHandleResponse(fullRequest, ctx, httpResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                    return httpResponse;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务端返回的获取的值，返回到浏览器端
     *
     * @param fullRequest
     * @param ctx
     * @param endpointResponse
     * @throws Exception
     */
    public void doHandleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    // response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            ctx.close();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
