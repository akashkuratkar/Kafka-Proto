package com.kafkaProto.replication;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class HandleCreateNewTopic {
    public static void handleRequest(HttpExchange exchange) throws IOException {
        System.out.println("Entered handle create new topic");
        URI requestURI = exchange.getRequestURI();
        JSONObject body = getBody(exchange);
        String topicId = body.getString("topicId");
        CreateTopicReplica.createNewTopic(topicId);
        PollLeader pollLeader = new PollLeader(topicId);
        pollLeader.start();
        Boolean result = CreateTopicReplica.createNewTopic(topicId);
        String response = "";
        if(result){
            response = "topic file created";
        }else{
            response = "topic creation  failed";
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
}
