package client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import extra.Message;
import payload.BasicBuilder;
import payload.MessageBuilder;

public class ConsoleListener extends Thread {
	private Socket _socket;
	private boolean _forever = true;
	private MessageBuilder _msgBuilder;
	private long _receievedCount = 0;

	private boolean _verbose = true;
	private String conName  = "";
	private String topicName  = "";
	private boolean topicSubscribedSuccess = false;

	public ConsoleListener(Socket socket, String conName, String topicName) {
		this._socket = socket;
		_msgBuilder = new BasicBuilder();
		this.conName = conName;
		this.topicName = topicName;
	}

	public void stopListening() {
		_forever = false;
		this.interrupt();
	}

	@Override
	public void run() throws RuntimeException {
		byte[] raw = new byte[2048];
		while (_forever) {
			try {
				//ping
				var builder = new BasicBuilder();
				if(topicSubscribedSuccess) {
					byte[] msg = builder.encode(MessageBuilder.MessageType.pullMsg, "", conName, topicName,"", null).getBytes();
					_socket.getOutputStream().write(msg);
					_socket.getOutputStream().flush();
				}
				
				byte[] raw1 = new byte[2048];
				int len = _socket.getInputStream().read(raw1);
//				if (len <= 0)
//					continue;

				// Reply from server
				String rs = new String(raw1);
				System.out.println("    RCV: " + rs);
				List<Message> list = builder.decode(new String(raw1, 0, len).getBytes());
				for (Message mesg : list) {
					String payld = mesg.getPayload();
					System.out.print(payld);
					if("Error".equals(payld)) {
						System.err.println("Error occurred on server side");
						break;
					} else if(payld.contains("Topic Subscribed Successfully")) {
						topicSubscribedSuccess = true;
						break;
					}
//					if (_verbose)
//						System.out.println("--> " + msg);
				}
				Thread.sleep(5000);
			} catch (Exception e
					) {
				System.err.println("Error occured:"+ e.getMessage());
				
				try {
					Thread.sleep(20000);
					ConsoleApp.getIpAddress();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	public long getReceievedCount() {
		return _receievedCount;
	}
}
