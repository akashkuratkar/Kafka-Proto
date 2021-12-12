package kafka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import kafka.comm.core.BasicSocketServer;


public class MasterMain {


	public static void main(String[] args) {
		var p = new Properties();
		var server = new BasicSocketServer(p);
		server.start();
	}
	
	public static void yesWeCan() {
		HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        Map<Object, Object> reqBody = new HashMap<>();
        reqBody.put("topicId","Ali");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(reqBody))
                .uri(URI.create("https://httpbin.org/post"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
	}
	


