package client;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

import payload.MessageBuilder;

/**
 * interactive console interface to the socket example.
 * 
 * @author gash
 * 
 */
public class ConsoleApp {
	static String portNumber = "8080";
	static String topicName = "";
	static String hostName = "127.0.0.1";
	private Properties _setup;
	private static String conName  = "";
	private static String _host="";

	public ConsoleApp(Properties setup) {
		this._setup = setup;
	}

	public void run() {
		var bc = new BasicClient(_setup);
		bc.setName(conName);
		bc.setTopicName(topicName);
		bc.startSession();
		boolean execute = true;
		Scanner sc = new Scanner(System.in);
		while (execute) {
			try {
				bc.sendMessage(topicName,MessageBuilder.MessageType.subscribeTopic);
				String exit = sc.next();
				if("exit".equals(exit))
					break;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("\nGoodbye");
		bc.stopSession();
	}

	public static void main(String[] args) {
		if(args.length!=3) {
			System.err.println("Your arguments must contain ip address, topic and consumer name to which you want to subscribe, separated by a space.");
			return;
		}
		String ipAdd[] = args[0].split(":");
		hostName = ipAdd[0];
		portNumber = ipAdd[1];
		
		topicName = args[1];
		if(topicName.length()==0) {
			System.err.println("Please restart Consumer with topic subscription");
			return;
		}
		conName = args[2];
		getIpAddress();
		
		
	}
	
	public static void getIpAddress(){

		try {
			Socket socket = new Socket(hostName, Integer.parseInt(portNumber));
			System.out.println("Connected!");
			InputStream inputStream = socket.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			String message = dataInputStream.readUTF();
			System.out.println("The message sent from the socket was: " + message);
			System.out.println("Closing sockets.");
			socket.close();
			_host = message;
			var p = new Properties();
			p.setProperty("host", _host);
			p.setProperty("port", "2100");
			var ca = new ConsoleApp(p);
			ca.run();
			return ;
		}
		catch(Exception e) {
			System.out.println("Not able to connect zookeeper");
		}




	}
}
