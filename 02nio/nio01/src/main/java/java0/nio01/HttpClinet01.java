package java0.nio01;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpClinet01 {
    public static void main(String[] args) throws IOException{
        Socket clientSocket = new Socket("127.0.0.1",8801);
        client(clientSocket);
    }
    
    private static void client(Socket socket) {
        try {
            // 获取服务端消息
            InputStream socketData = socket.getInputStream();
            //读取流数据 (socket返回的数据)
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = socketData.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len));
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}