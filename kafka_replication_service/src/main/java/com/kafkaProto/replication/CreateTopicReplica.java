package com.kafkaProto.replication;

import org.json.JSONObject;
import resources.ReplicaServiceConfig;
import resources.ReplicaUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateTopicReplica {

    public static Boolean create(String ip, String topicId) {
        // TODO : create API call to create new topic in specied ip broker
        System.out.println("entered create ip method");
        return makeAPICall(ip,topicId);
        // new API call to create-new-topic
//        return true;
    }

    private static boolean makeAPICall(String ip, String topicId){
        try {
            String formedUrl = "http://"+ip+":"+ ReplicaServiceConfig.REPLICA_SERVICE_PORT+"/create-new-topic";
            //String formedUrl ="http://localhost:5676/get_topic_leader/";
            System.out.println("get data url formed : " + formedUrl);
            URL url = new URL(formedUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            JSONObject cred = new JSONObject();
            JSONObject parent=new JSONObject();
            cred.put("topicId",topicId);

            OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
            wr.write(cred.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
               return true;
            } else {
                System.out.println(conn.getResponseMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
            return false;
        }
    }

    public static Boolean createNewTopic(String topicId) {
        System.out.println("Got a request for "+topicId);
        String FilePath = ReplicaUtils.getTopicPath(topicId);
        File temp = new File(FilePath);
        temp.getParentFile().mkdirs(); // creates directory if not exist
        boolean exists = temp.exists();
        if (exists) {
            System.out.println("Topic Already exist");
            return true;
        } else {
            try {
                temp.createNewFile();
                return true;
            } catch (IOException e) {
                System.out.println("Creation of topic failed");
                return false;
            }

        }
    }
}


