package io.github.tn.week03_3.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author tn
 * @version 1
 * @ClassName HttpRequestFilterimpl
 * @description 拦截器
 * @date 2020/11/3 20:16
 */
public class HttpRequestFilterImpl implements HttpRequestFilter {


    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        //如果同名header已存在，则追加至原同名header后面
        fullRequest.headers().add("nio","tanning");
        //如果同名header已存在，则覆盖一个同名header。
//       fullRequest.headers().set("nio","tanning");
    }
}
