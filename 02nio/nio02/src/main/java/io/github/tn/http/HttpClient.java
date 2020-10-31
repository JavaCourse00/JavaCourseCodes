package io.github.tn.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 *  周四作业：整合你上次作业的httpclient/okhttp
 * @author tn
 * @version 1
 * @ClassName HttpClient
 * @description 访问http
 * @date 2020/10/27 21:25
 */
public class HttpClient {


    //server
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final ServerSocket serverSocket = new ServerSocket(8803);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //service
    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = requset();
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // invoking
    private static String requset(){
        HttpGet httpGet = new HttpGet("http://localhost:8801/test");
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse resp = httpClient.execute(httpGet) ) {
            if(resp.getStatusLine().getStatusCode()==200){
                HttpEntity body = resp.getEntity();
                //使用工具类EntityUtils，从响应中取出实体表示的内容并转换成字符串
                String data = EntityUtils.toString(body, "utf-8");
                return data;
            }
        }catch (Exception e){
            System.err.println("接口调用失败");
        }
        return null;
    }



}
