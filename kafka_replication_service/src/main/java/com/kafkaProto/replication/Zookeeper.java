package com.kafkaProto.replication;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import resources.ReplicaServiceConfig;

public class Zookeeper {

    public static Boolean sendTopicReplica(JSONObject reqBody){
        // TODO send topic data (leader, replica ips to zookeeper)
        try {
            // TODO Change URL to the URL received by Zookeeper team
            String zookeeperUrl = "http://"+ReplicaServiceConfig.ZOOKEEPER_HOST+":"+ReplicaServiceConfig.ZOOKEEPER_PORT+ReplicaServiceConfig.ZOOKEEPER_GET_LEADER_ENDPOINT;
            System.out.println("Url formed : " + zookeeperUrl);
            URL url = new URL(zookeeperUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            String data = "";
            while ((output = br.readLine()) != null) {
                data += output;
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
            return null;
        }
        // Checking data
        System.out.println(reqBody.toString());
        System.out.println(reqBody.get("topicId"));
        System.out.println(reqBody.get("leader"));
        System.out.println(reqBody.get("replica"));
        return true;
    }


    public static String getDataFromZookeeper(String topicId){
        try {
            String zookeeperUrl = "http://"+ReplicaServiceConfig.ZOOKEEPER_HOST+":"+ReplicaServiceConfig.ZOOKEEPER_PORT+ReplicaServiceConfig.ZOOKEEPER_GET_LEADER_ENDPOINT+"/";
            System.out.println("url formed : " + zookeeperUrl);
            URL url = new URL(zookeeperUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            String data = "";
            while ((output = br.readLine()) != null) {
                data += output;
            }
            conn.disconnect();
            return data;

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
            return null;
        }
    }
}
