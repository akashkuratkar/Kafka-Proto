package gash.app.producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import gash.app.client.BasicClient;
import gash.app.client.ConsoleApp;
import gash.app.client.HeartBeat;
import gash.comm.extra.TopicMessage;
import gash.comm.payload.BasicBuilder;
import gash.comm.payload.MessageBuilder;

public class Driver {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		if(args.length!=3) {
			System.out.println("The length of args is less than 3");
			System.exit(0);
		}
		System.out.println(args[2]);
		var p = new Properties();
		p.setProperty("host", args[0]);
		p.setProperty("port", args[1]);
		
		BufferedReader reader;
		Date date = new Date();
		var ca = new ConsoleApp(p);
		BasicClient bc = new BasicClient(ca.get_setup());
		bc.startSession();
		try {
			reader = new BufferedReader(new FileReader(args[2]));
			String line = reader.readLine();
			int i=0;
			String topic = "";
			String message = "";
			Producer prod = new Producer();
			Topic t = null;
			TopicMessage tm;
			while(line!=null) {
				System.out.println(line);
				if(i%2==0) {
					topic = line;
					t = prod.createTopic(topic);
					prod.sendMessage(bc, t);
				}else {
					message = line;
					tm = new TopicMessage(topic,message,""+i);
					prod.sendMessage(bc, tm);
					
				}
				line = reader.readLine();
				i++;
			}
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
