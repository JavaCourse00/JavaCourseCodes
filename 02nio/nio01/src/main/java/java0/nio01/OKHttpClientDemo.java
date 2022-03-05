package java0.nio01;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OKHttpClientDemo {

    private static OkHttpClient client = new OkHttpClient();
    public static void main(String[] args) throws IOException {

        getBody1(client, "http://localhost:8801");
        client = null;
    }

    private static void getBody1(OkHttpClient client, String url){

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        //String body = "test";
        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client = null;
        }
    }
}
