package gash.app.client;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import gash.comm.core.Settings;
import gash.comm.extra.Message;
import gash.comm.extra.TopicMessage;
import gash.comm.payload.BasicBuilder;
import gash.comm.payload.MessageBuilder;

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
	private int id;

	private String _host = "localhost"; // "127.0.0.1" ;
	private ConsoleListener _listener;
	private Socket _socket;
	private String _name;
	//Queue<byte []> queue;
	Queue<String> queue;
	/**
	 * empty constructor
	 */
	public BasicClient() {
		queue = new LinkedList<>();
	}

	/**
	 * specify the host and port to connect to
	 */
	public BasicClient(Properties setup) {
		this._setup = setup;
		queue = new LinkedList<>();
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getName() {
		return _name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
			_listener = new ConsoleListener(_socket);
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
			byte[] msg = builder.encode(MessageBuilder.MessageType.leave, getID(), ""+getId(), null,"", null).getBytes();
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
	public void join(String name) {
		if (_socket == null) {
			System.out.println("message not sent");
			return;
		}

		try {
			var builder = new BasicBuilder();
			byte[] msg = builder.encode(MessageBuilder.MessageType.join, getID(), ""+getId(), null,"", null).getBytes();
			_socket.getOutputStream().write(msg);
			_socket.getOutputStream().flush();
			_sentCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * send a general (public) message to the server
	 * 
	 * @param msg
	 *            String
	 */
	public void sendMessage(String message) {
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
			byte[] msg = builder.encode(MessageBuilder.MessageType.msg, ""+getId(), _name, message,"2", null).getBytes();
			_socket.getOutputStream().write(msg);
			_socket.getOutputStream().flush();
			_sentCount++;
			
			//var listener = new ConsoleListener(_socket);
			//listener.run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(TopicMessage tm) {
		if (_socket == null) {
			System.out.println("message not sent");
			return;
		} else if (tm.getMessage() != null && tm.getMessage().length() > 1024) {
			System.out.println("message exceeds 1024 size limit");
			return;
		}

		try {
			if (_socket.isOutputShutdown()) {
				System.out.println("ERROR: _socket write is blocked!");
				// TODO wait for writable again
			}
			
			var builder = new BasicBuilder();
			byte[] msg = builder.encode(MessageBuilder.MessageType.msg, getID(), ""+getId(), tm.getMessage(),"2", null).getBytes();
			//if(type.equalsIgnoreCase("create")) {
				//msg = builder.encode(MessageBuilder.MessageType.sendMessage, tm.getMessageID(), tm.getTopicName(), tm.getMessage(),"2", null).getBytes();
				String msg1 = builder.encode(MessageBuilder.MessageType.sendMessage, tm.getMessageID(), tm.getTopicName(), tm.getMessage(),"2", null);
				
				//}else if(to.equalsIgnoreCase("topic")) {
				//msg = builder.encode(MessageBuilder.MessageType.topic, getID(), ""+getID(), message,"2", null).getBytes();
			//}
			queue.add(msg1);
			if(queue.size()==3) {
				StringBuilder sb = new StringBuilder();
				//sb.append("*^&$");
//				byte[] res = queue.poll();
				for(String msgi:queue) {
					
					sb.append(msgi);
					//sb.append("\\[");
					System.out.println(msgi.toString());
//					_socket.getOutputStream().write(msgi);
//					_socket.getOutputStream().flush();
//					_sentCount++;
//					
				}
			System.out.println(sb.toString().getBytes());
				_socket.getOutputStream().write(sb.toString().getBytes());
				_socket.getOutputStream().flush();
				_sentCount++;
				queue.removeAll(queue);
			}
			//var listener = new ConsoleListener(_socket);
			//listener.run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(String to, String message) {
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
			byte[] msg = builder.encode(MessageBuilder.MessageType.msg, getID(), ""+getId(), message,"2", null).getBytes();
			if(to.equalsIgnoreCase("create")) {
				msg = builder.encode(MessageBuilder.MessageType.all, getID(), ""+getID(), message,"2", null).getBytes();
			}else if(to.equalsIgnoreCase("topic")) {
				msg = builder.encode(MessageBuilder.MessageType.createTopic, getID(), ""+getID(), message,"2", null).getBytes();
			}
			_socket.getOutputStream().write(msg);
			_socket.getOutputStream().flush();
			_sentCount++;
			
			//var listener = new ConsoleListener(_socket);
			//listener.run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * for use with integrating to other code
	 * 
	 * @param msg
	 */
	public void sendMessage(Message msg) throws Exception{

		//try {
			var builder = new BasicBuilder();
			byte[] raw = builder
					.encode(msg.getType(), msg.getMid(), msg.getSource(), msg.getPayload(),msg.getDestination(), msg.getReceived())
					.getBytes();
			
			_socket.getOutputStream().write(raw);
			_socket.getOutputStream().flush();
			// Akash K. is suggesting to add queue here. 
			_sentCount++;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
