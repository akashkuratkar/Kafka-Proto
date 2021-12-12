package client;

import java.net.Socket;
import java.util.List;
import java.util.Properties;

import core.Settings;
import extra.Message;
import payload.BasicBuilder;
import payload.MessageBuilder;

/**
 * client chat
 * 
 * @author gash
 * 
 */
public class BasicClient {
	private Properties _setup;

	private long _count = 0l;
	private long _sentCount = 0l;

	private String _host = "localhost"; // "127.0.0.1" ;
	private ConsoleListener _listener;
	private Socket _socket;
	private String _name;

	private String topicName;
	
	public void setTopicName(String name) {
		this.topicName = name;
	}
	/**
	 * empty constructor
	 */
	public BasicClient() {
	}

	/**
	 * specify the host and port to connect to
	 */
	public BasicClient(Properties setup) {
		this._setup = setup;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getName() {
		return _name;
	}

	/**
	 * connect to server
	 */
	public void startSession() {
		if (_socket != null) {
			return;
		}

		String host = _setup.getProperty(Settings.PropertyHost);
		String port = _setup.getProperty(Settings.PropertyPort);
		if (host == null || port == null)
			throw new RuntimeException("Missing port and/or host");

		try {
			_socket = new Socket(host, Integer.parseInt(port));
			System.out.println("Connected to " + _socket.getInetAddress().getHostAddress());

			// establish response handler
			_listener = new ConsoleListener(_socket, _name, topicName);
			_listener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * close this session
	 */
	public void stopSession() {
		if (_socket == null) {
			System.out.println("message not sent");
			return;
		}

		try {
			if (_listener != null)
				_listener.stopListening();

			var builder = new BasicBuilder();
			byte[] msg = builder.encode(MessageBuilder.MessageType.leave, getID(), _name, null,"", null).getBytes();
			_socket.getOutputStream().write(msg);
			_socket.getOutputStream().flush();
			_socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		_socket = null;
	}

	/**
	 * announce that client has joined the network
	 * 
	 * @param _name
	 *            String
	 */
//	public void join(String name) {
//		if (_socket == null) {
//			System.out.println("message not sent");
//			return;
//		}
//
//		try {
//			var builder = new BasicBuilder();
//			byte[] msg = builder.encode(MessageBuilder.MessageType.join, getID(), name, null, null).getBytes();
//			_socket.getOutputStream().write(msg);
//			_socket.getOutputStream().flush();
//			_sentCount++;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * send a general (public) message to the server
	 * 
	 * @param msg
	 *            String
	 */
	public void sendMessage(String message, MessageBuilder.MessageType msgType) {
		if (_socket == null) {
			System.out.println("message not sent");
			return;
		} else if (message != null && message.length() > 1024) {
			System.out.println("message exceeds 1024 size limit");
			return;
		}

		try {
			if (_socket.isOutputShutdown()) {
				System.out.println("ERROR: _socket write is blocked!");
				// TODO wait for writable again
			}
			
			var builder = new BasicBuilder();
			byte[] msg = builder.encode(msgType, getID(), _name, message,"", null).getBytes();
			_socket.getOutputStream().write(msg);
			_socket.getOutputStream().flush();
			_sentCount++;
			
			byte[] raw = new byte[2048];
			int len = _socket.getInputStream().read(raw);
//			if (len <= 0)
//				continue;

			// Reply from server
			String rs = new String(raw);
			System.out.println("    RCV: " + rs);
			List<Message> list = builder.decode(new String(raw, 0, len).getBytes());
			for (Message mesg : list) {
				System.out.print(mesg.getPayload());
//				if (_verbose)
//					System.out.println("--> " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * for use with integrating to other code
	 * 
	 * @param msg
	 */
	public void sendMessage(Message msg) {

		try {
			var builder = new BasicBuilder();
			byte[] raw = builder
					.encode(msg.getType(), msg.getMid(), msg.getSource(), msg.getPayload(),"", msg.getReceived())
					.getBytes();

			_socket.getOutputStream().write(raw);
			_socket.getOutputStream().flush();
			_sentCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * are there messages we are waiting for responses?
	 * 
	 * @return
	 */
	public boolean isPendingResponses() {
		System.out.println(_sentCount + " / " + _listener.getReceievedCount());
		return _sentCount != _listener.getReceievedCount();
	}

	private String getID() {
		return Long.toString(_count++);
	}
}
