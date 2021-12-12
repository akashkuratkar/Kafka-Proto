package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

	public ConsoleApp(Properties setup) {
		this._setup = setup;
	}

	public void run() {

		var br = new BufferedReader(new InputStreamReader(System.in));

//		String name = null;
//		do {
//			try {
//				if (name == null) {
//					System.out.print("Enter your name in order to join: ");
//					System.out.flush();
//					name = br.readLine();
//				}
//				System.out.println("");
//			} catch (Exception e2) {
//			}
//
//			if (name != null)
//				break;
//		} while (true);

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
		var p = new Properties();
		p.setProperty("host", hostName);
		p.setProperty("port", portNumber);

		var ca = new ConsoleApp(p);
		ca.run();
	}
}
