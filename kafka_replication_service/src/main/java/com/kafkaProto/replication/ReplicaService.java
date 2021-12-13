package com.kafkaProto.replication;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import resources.ReplicaServiceConfig;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static resources.ReplicaUtils.getTopicsFolder;

public class ReplicaService {
    private static HttpServer server;
    public static void main(String[] args) throws IOException {
        startReplicaServer();
        addTopicReplicationApi();
        addSyncWithLeaderApi();
        addStartPollingApi();
        addCreateNewTopic();
        initPolling();
    }

    private static void startReplicaServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(ReplicaServiceConfig.REPLICA_SERVICE_PORT), 0);
        server.start();
        System.out.println("Replica server started on port "+ ReplicaServiceConfig.REPLICA_SERVICE_PORT);
    }

    private static void initPolling() {
        ArrayList<String> existingTopics = getExistingTopics();
        for(int i=0; i<existingTopics.size();i++){
            PollLeader pollLeader = new PollLeader(existingTopics.get(i));
            pollLeader.start();
        }
    }

    private static ArrayList<String> getExistingTopics() {
        File folder = new File(getTopicsFolder());
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> topicsList = new ArrayList<>();
        if(listOfFiles == null){
            return topicsList;
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                topicsList.add(listOfFiles[i].getName().replace(".csv",""));
            }
        }
        return topicsList;
    }

    private static void addTopicReplicationApi(){
        HttpContext context = server.createContext("/create-topic-replica");
            context.setHandler(HandleTopicReplica::handleRequest);
    }

    private static void addSyncWithLeaderApi(){
        HttpContext context = server.createContext("/leader-sync");
        context.setHandler(LeaderSync::handleRequest);
    }

    private static void addStartPollingApi(){
        HttpContext context = server.createContext("/start-polling");
        context.setHandler(LeaderSync::handleRequest);
    }

    private static void addCreateNewTopic(){
        HttpContext context = server.createContext("/create-new-topic");
        context.setHandler(HandleCreateNewTopic::handleRequest);
    }
}
