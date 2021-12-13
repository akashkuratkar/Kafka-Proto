package resources;

import resources.ReplicaServiceConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReplicaUtils {
    public static String getTopicPath(String TopicName){
        String path = getTopicsFolder()  + TopicName + ReplicaServiceConfig.FORMAT_LOG;
        return path;
    }
    public static String getTopicsFolder(){
        String path = ReplicaServiceConfig.PATH_TOPICS ;
        return path;
    }

    public static int getLinesCount(String path) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.readLine() != null) lines++;
        } catch (FileNotFoundException e){
            System.out.println("Topic not exist in local server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
