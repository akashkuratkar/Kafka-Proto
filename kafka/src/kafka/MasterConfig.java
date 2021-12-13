package kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kafka.comm.models.Subscribe;

public class MasterConfig {
	public static Map<String,List<Subscribe>>topic_list = new HashMap<>();
	
	public static String convertMapToString(Map<String, List<Subscribe>> map) {
        StringBuilder mapAsString = new StringBuilder();
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + convertObject(map.get(key)) + "; ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length());
        return mapAsString.toString();
    }

    private static String convertObject(List<Subscribe> subscribers) {

        StringBuilder str= new StringBuilder("[");
        for(Subscribe subscriber : subscribers)
        {
            str.append(subscriber.getSubscribe_id()).append(":").append(subscriber.getOffset()).append(":{");
        }
        str.append("]");
        return str.toString();
    }



    public static Map<String, List<Subscribe>> convertStringToMap(String mapAsString)
    {    System.out.println(mapAsString);
        Map<String, String> map = Arrays.stream(mapAsString.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));

        Map<String, List<Subscribe>> result= new HashMap();
        for (Map.Entry<String,String> entry : map.entrySet())
        {
            String ans= entry.getValue();

            ans = ans.replaceAll("[\\[\\]\\{\\}]","");
            System.out.println(ans);
            String[] stringList= ans.split(":");

            List<Subscribe> subscribers = getList(stringList);

            result.put(entry.getKey(),subscribers);
        }
        return result;
    }

    private static List<Subscribe> getList(String[] stringList) {
        int len= stringList.length/2;
        List<Subscribe> subscribers = new LinkedList<>();
        for(int i=0;i<len;i+=2)
        {
        	Subscribe subscriber= new Subscribe();
            subscriber.setSubscribe_id(stringList[i]);
            subscriber.setOffset(Integer.valueOf(stringList[i+1]));
            subscribers.add(subscriber);
        }

   return subscribers;
    }

	/*public static String convertMapToString(Map<String, List<Subscribe>> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ": ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
    
        public static Map<String, List<String>> convertStringToMap(String mapAsString)
    {    System.out.println(mapAsString);
        Map<String, String> map = Arrays.stream(mapAsString.split(":"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));

        Map<String, List<String>> result= new HashMap();
        for (Map.Entry<String,String> entry : map.entrySet())
        {
            String ans= entry.getValue();

            ans = ans.replaceAll("[\\[\\]\\{\\}]","");
            System.out.println(ans);
            String[] stringList= ans.split(",");
            List<String> list= Arrays.asList(stringList);
            result.put(entry.getKey(),list);
        }
        return result;
    }*/
}
