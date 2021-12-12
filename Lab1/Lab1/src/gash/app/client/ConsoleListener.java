package gash.app.client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import gash.comm.extra.Message;
import gash.comm.payload.BasicBuilder;
import gash.comm.payload.MessageBuilder;

public class ConsoleListener extends Thread {
	private Socket _socket;
	private boolean _forever = true;
	private MessageBuilder _msgBuilder;
	private long _receievedCount = 0;

	private boolean _verbose = true;

	public ConsoleListener(Socket socket) {
		this._socket = socket;
		_msgBuilder = new BasicBuilder();
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
				int len = _socket.getInputStream().read(raw);
				if (len <= 0)
					continue;

				// Reply from server
				String rs = new String(raw);
				System.out.println("    RCV: " + rs);

				try {
					List<Message> list = _msgBuilder.decode(new String(raw, 0, len).getBytes());
					for (Message msg : list) {
						_receievedCount++;
						if (_verbose)
							System.out.println("--> " + msg.getPayload());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO report but, continue
			}
		}

	}

	public long getReceievedCount() {
		return _receievedCount;
	}
}
