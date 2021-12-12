package gash.app.server;

import java.util.Properties;

import gash.comm.core.BasicSocketServer;
import gash.comm.core.Settings;

/**
 * server application
 * 
 * @author gash
 * 
 */
class ServerApp {
	public ServerApp() {
	}

	public static void main(String[] args) {
		// default settings come from the constructor of the socket
		var p = new Properties();

		// override default args, parsing to set timeouts, port, ...
		// TODO replace with a robust arg processor class like bash's
		// https://www.redhat.com/sysadmin/arguments-options-bash-scripts
		for (int n = 0; n < args.length; n += 2) {
			if (Settings.PropertyPort.equalsIgnoreCase(args[n])) {
				p.setProperty(args[n], args[n + 1]);
			} else if (Settings.PropertyTimeout.equalsIgnoreCase(args[n])) {
				p.setProperty(args[n], args[n + 1]);
			} else if (Settings.PropertyFreq.equalsIgnoreCase(args[n])) {
				p.setProperty(args[n], args[n + 1]);
			}
		}

		var server = new BasicSocketServer(p);
		server.start();
	}
}
