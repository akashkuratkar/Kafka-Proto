package kafka.app.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * interactive console interface to the socket example.
 * 
 * @author gash
 * 
 */
public class ConsoleApp {
	private Properties _setup;
	private int count=0;

	public ConsoleApp(Properties setup) {
		this._setup = setup;
	}

	public void run() {

		var br = new BufferedReader(new InputStreamReader(System.in));

		String name = null;
		do {
			try {
				if (name == null) {
					System.out.print("Enter your name in order to join: ");
					System.out.flush();
					name = br.readLine();
				}
				System.out.println("");
			} catch (Exception e2) {
			}

			if (name != null)
				break;
		} while (true);

		var bc = new BasicClient(_setup);
		
		bc.startSession();
		bc.setName(name);
		bc.join(name);
		bc.setId(count++);
		HeartBeat beat = new HeartBeat(bc,1000,1000);
		beat.start();
		System.out.println("Conenected to keeper!");

		boolean forever = true;
		while (forever) {
			try {
				
				String choice = br.readLine();

				System.out.println("");

				if (choice == null) {
					continue;
				} else if (choice.equalsIgnoreCase("whoami")) {
					System.out.println("You are " + bc.getName());
				} else if (choice.equalsIgnoreCase("exit")) {
					System.out.println("EXIT CMD!");
					bc.stopSession();
					forever = false;
				} else if (choice.equalsIgnoreCase("post")) {
					System.out.print("To?: [id/all]");
					String to = br.readLine();
					System.out.print("Enter message: ");
					String msg = br.readLine();
					bc.sendMessage(to,msg);
				} else if (choice.equalsIgnoreCase("help")) {
					System.out.println("");
					System.out.println("Commands");
					System.out.println("-------------------------------");
					System.out.println("help - show this menu");
					System.out.println("post - send a message");
					System.out.println("exit - end session");
					System.out.println("");
				} else {
					bc.sendMessage(choice);
				}

				// System.out.println( "" ) ;
			} catch (Exception e) {
				forever = false;
				e.printStackTrace();
			}
		}

		
	}

	public Properties get_setup() {
		return _setup;
	}

	public void set_setup(Properties _setup) {
		this._setup = _setup;
	}

	public static void main(String[] args) {
		var p = new Properties();
		p.setProperty("host", "127.0.0.1");
		p.setProperty("port", "2100");

		var ca = new ConsoleApp(p);
		ca.run();
	}
}
