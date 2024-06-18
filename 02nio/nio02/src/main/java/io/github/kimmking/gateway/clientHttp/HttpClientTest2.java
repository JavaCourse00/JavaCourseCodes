package io.github.kimmking.gateway.clientHttp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientTest2 {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String url = "http://localhost:8801";
		get(url);
	}
	
	public static String get(String url) throws ClientProtocolException, IOException {
		long startTime=System.currentTimeMillis();

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(URI.create(url));
        HttpResponse response = httpClient.execute(httpGet);
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        
        long endTime=System.currentTimeMillis();
		float excTime=(float)(endTime-startTime)/1000;
		System.out.println("执行时间为："+excTime+"s");
		
        return httpEntityContent;
    }

	private static String getHttpEntityContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream is = entity.getContent();
	            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	            String line = br.readLine();
	            StringBuilder sb = new StringBuilder();
	            while (line != null) {
	                sb.append(line + "\n");
	                line = br.readLine();
	            }
	            br.close();
	            is.close();
	            return sb.toString();
	        }
	        return "";
	    }

	 
}
