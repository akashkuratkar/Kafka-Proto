package kafka;

import java.util.Properties;

import kafka.comm.core.BasicSocketServer;

public class MasterMain {


	public static void main(String[] args) {
		var p = new Properties();
		String ipAdd[] = args[0].split(":");
		p.setProperty("host", ipAdd[0]);
		p.setProperty("zooport",  ipAdd[1]);
		p.setProperty("port",  "2100");
		var server = new BasicSocketServer(p);
		server.start();
	}
}
