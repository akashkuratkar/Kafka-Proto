package com.kafkaProto.replication;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import resources.ReplicaUtils;

public class LeaderSync {
    public static void handleRequest(HttpExchange exchange) throws IOException{
        // req body will have latest replica offset
        JSONObject body = getBody(exchange);
        String topicId = body.getString("topicId");
        int replicaOffset = body.getInt("replicaOffset");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(ReplicaUtils.getTopicPath(topicId)));
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            String response = "";
            if(lines.size()>replicaOffset){
                for(int i = replicaOffset;i<lines.size();i++){
                    response+=lines.get(i)+System.lineSeparator();
                }
            }
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
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
