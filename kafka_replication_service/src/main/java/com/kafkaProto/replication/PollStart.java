package com.kafkaProto.replication;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class PollStart {

    public static void handleRequest(HttpExchange exchange) throws IOException {
        JSONObject body = getBody(exchange);
        PollLeader pollLeader = new PollLeader(body.getString("topicId"));
        pollLeader.start();
    }

    private static JSONObject getBody(HttpExchange exchange) throws IOException{
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
