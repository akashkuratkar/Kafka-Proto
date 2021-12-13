package gash.app.producer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import gash.app.client.BasicClient;
import gash.app.client.ConsoleApp;
import gash.app.client.HeartBeat;
import gash.comm.extra.TopicMessage;
import gash.comm.payload.BasicBuilder;
import gash.comm.payload.MessageBuilder;

public class Driver {

	public static void getIpAddress() throws IOException {

		Socket socket = new Socket("localhost", 7777);
		System.out.println("Connected!");
		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		String message = dataInputStream.readUTF();
		System.out.println("The message sent from the socket was: " + message);
		System.out.println("Closing sockets.");
		socket.close();


	}



	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		while(true) {
			var p = new Properties();


			p.setProperty("host", "172.20.10.12");

			p.setProperty("port", "2100");



			var ca = new ConsoleApp(p);
			BasicClient bc = new BasicClient(ca.get_setup());
			try {


				bc.startSession();
//		HeartBeat beat = new HeartBeat(bc,1000,1000);
//		beat.start();
				System.out.println("Conenected to keeper!");
				Producer prod = new Producer();
				BasicBuilder bbld = new BasicBuilder();
				Topic t = prod.createTopic("Ali123");
				//System.out.println(t.toString());
				prod.sendMessage(bc, t);
				TopicMessage tm = new TopicMessage("Ali123", "Awesome123", "127371237172");
				//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
				System.out.println(tm.toString());
				//System.out.println(awesome);
				prod.sendMessage(bc, tm);
				System.out.println();

				TopicMessage tm1 = new TopicMessage("Ali123", "Awesome456", "127371237172");
				//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
				System.out.println(tm.toString());
				//System.out.println(awesome);
				//Thread.sleep(20);
				System.out.println("After sleep");
				prod.sendMessage(bc, tm1);
				System.out.println();

				TopicMessage tm2 = new TopicMessage("Ali123", "Awesome789", "127371237172");
				//String awesome = bbld.encode(MessageBuilder.MessageType.msg, "1", "G.O.A.T","God of Lords", "Ali1",date,tm );
				System.out.println(tm.toString());
				//System.out.println(awesome);
				prod.sendMessage(bc, tm2);
				System.out.println();

				Thread.sleep(10000);


				Topic t2 = prod.createTopic("Akash1235");
				//System.out.println(t.toString());
				prod.sendMessage(bc, t2);
				TopicMessage tm3 = new TopicMessage("Akash1235", "This is a new Message", "127371237172");

				prod.sendMessage(bc, tm3);
				bc.stopSession();
			} catch (Exception e) {

				continue;
			}
		}
	}

}
