package gash.app.producer;

import java.util.Date;
import java.util.Properties;

import gash.app.client.BasicClient;
import gash.app.client.ConsoleApp;
import gash.app.client.HeartBeat;
import gash.comm.extra.TopicMessage;
import gash.comm.payload.BasicBuilder;
import gash.comm.payload.MessageBuilder;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		var p = new Properties();
		p.setProperty("host", "127.0.0.1");
		p.setProperty("port", "2100");
		
		
		Date date = new Date();
		var ca = new ConsoleApp(p);
		BasicClient bc = new BasicClient(ca.get_setup());
		bc.startSession();
//		HeartBeat beat = new HeartBeat(bc,1000,1000);
//		beat.start();
		System.out.println("Conenected to keeper!");
		Producer prod = new Producer();
		BasicBuilder bbld = new BasicBuilder();
		Topic t = prod.createTopic("Ali");
		//System.out.println(t.toString());
		prod.sendMessage(bc, t);
//		TopicMessage tm = new TopicMessage("Ali","Awesome123","127371237172");
//		//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
//		System.out.println(tm.toString());
//		//System.out.println(awesome);
//		prod.sendMessage(bc, tm);
//		System.out.println();
//		
//		TopicMessage tm1 = new TopicMessage("Ali","Awesome456","127371237172");
//		//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
//		System.out.println(tm.toString());
//		//System.out.println(awesome);
//		prod.sendMessage(bc, tm1);
//		System.out.println();
//		
//		TopicMessage tm2 = new TopicMessage("Ali","Awesome789","127371237172");
//		//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
//		System.out.println(tm.toString());
//		//System.out.println(awesome);
//		prod.sendMessage(bc, tm2);
//		System.out.println();
		
		
		Topic t1 = prod.createTopic("Ali");
		//System.out.println(t.toString());
		prod.sendMessage(bc, t1);

	}

}
