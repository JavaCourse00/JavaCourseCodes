//package io.github.kimmking.gateway.outbound;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.HttpRequestEncoder;
//import io.netty.handler.codec.http.HttpResponseDecoder;
//
//public class NettyHttpClient {
//    public void connect(String host, int port) throws Exception {
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(workerGroup);
//            b.channel(NioSocketChannel.class);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
//                     客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new HttpClientOutboundHandler());
//                }
//            });
//            
//            // Start the client.
//            ChannelFuture f = b.connect(host, port).sync();
//            
//           // for test
//            URI uri = new URI("http://127.0.0.1:8844");
//            String msg = "Are you ok?";
//            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//                    uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
//            
//            // 构建http请求
//            request.headers().set(HttpHeaders.Names.HOST, host);
//            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
//            // 发送http请求
//            f.channel().write(request);
//            f.channel().flush();
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
//        
//    }
//    
//    public static void main(String[] args) throws Exception {
//        NettyHttpClient client = new NettyHttpClient();
//        client.connect("127.0.0.1", 8844);
//    }
//}