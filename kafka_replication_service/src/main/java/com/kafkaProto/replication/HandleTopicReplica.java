package com.kafkaProto.replication;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.*;
import resources.BrokerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleTopicReplica {

    public static void handleRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        JSONObject body = getBody(exchange);
        String topicId = body.getString("topicId");
        int replicationFactor = body.getInt("replicationFactor");
        String myIp = BrokerInfo.getMyIp();
        List<String> hosts = BrokerInfo.getHosts();
        List<Boolean> results = new ArrayList<>();
        List<String> selectedHosts = new ArrayList<>();
        boolean allSuccess = true;
        replicationFactor = replicationFactor -1 ;
        if(hosts.size() >= replicationFactor){
            for(int i=0,j=0;i<replicationFactor;j++){
                if(!hosts.get(j).equals(myIp)){
                    results.add(CreateTopicReplica.create(hosts.get(j),topicId));
                    selectedHosts.add(hosts.get(j));
                    i++;
                }
            }
            for(Boolean result : results){
                if(!result){
                    allSuccess = false;
                }
            }
        }else{
            allSuccess = false;
        }

        if(allSuccess){
            // Send data to zookeeper
            // req body --> {topicId:"topicId", leader:"192.168.0.1", replica:["192.168.0.1","192.168.0.1"]}
            JSONObject zookeeperBody = new JSONObject();
            zookeeperBody.put("topicId",topicId);
            zookeeperBody.put("leader",myIp);
            zookeeperBody.put("replica",selectedHosts);
//            boolean zookeeperUpdated = Zookeeper.sendTopicReplica(zookeeperBody);
            //TODO update zookeeper code
            boolean zookeeperUpdated = true;
            if(zookeeperUpdated){
                // TODO check below commented code;
//                for(String host : selectedHosts){
//                    HttpClient client = HttpClient.newBuilder()
//                            .version(HttpClient.Version.HTTP_2)
//                            .connectTimeout(Duration.ofSeconds(10))
//                            .build();
//                    Map<Object, Object> reqBody = new HashMap<>();
//                    reqBody.put("topicId",topicId);
//                    HttpRequest request = HttpRequest.newBuilder()
//                            .POST(ofFormData(reqBody))
//                            .uri(URI.create("https://httpbin.org/post"))
//                            .build();
//                    try {
//                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                // Send response to caller api
                String response = replicationFactor+ " replicas created for topic id "+topicId;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }else{
                System.out.println("Zookeeper down!!!");
            }
        }else{
            System.out.println("All replicas not created!!!!!");
        }
    }
    private static JSONObject getBody(HttpExchange exchange) throws IOException {
        System.out.println("-- body --");
        InputStream is = exchange.getRequestBody();
        Headers requestHeaders = exchange.getRequestHeaders();
        int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
        byte[] data = new byte[contentLength];
        int length = is.read(data);
        String stringData = new String(data);
        JSONObject jsonObject = new JSONObject(stringData);
        return jsonObject;
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

