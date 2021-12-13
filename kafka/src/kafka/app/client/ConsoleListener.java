package kafka.app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.MasterConfig;
import kafka.comm.models.Subscribe;

public class ConsoleListener extends Thread {
	private Socket _socket;
	private boolean _forever = true;
	private long _receievedCount = 0;
	private ObjectMapper objectMapper;


	public ConsoleListener(Socket socket) {
		this._socket = socket;
		 objectMapper =new ObjectMapper();
	}

	public void stopListening() {
		_forever = false;
		this.interrupt();
	}

	@Override
	public void run() throws RuntimeException {
	
		while (_forever) {
			try {
				 InputStream inputStream = _socket.getInputStream();
			        DataInputStream dataInputStream = new DataInputStream(inputStream);
			        String message = dataInputStream.readUTF();
				if (message.length() <= 0)
					continue;

				// Reply from server
				System.out.println("    RCV: " + message);

				try {
					String reply="";
					String [] messages = message.split(" ");
					if(messages[0].equalsIgnoreCase("False")) {
						if(MasterConfig.topic_list.size()>0) {
						 reply =objectMapper.writeValueAsString(MasterConfig.topic_list);
						}
						//String json = new ObjectMapper().writeValueAsString(map);
						sendMessage(reply);
					}else {
						MasterConfig.topic_list =objectMapper.readValue(messages[1],new TypeReference<Map<String,List< Subscribe>>>(){});
						reply = MasterConfig.convertMapToString(MasterConfig.topic_list);
						sendMessage(messages[1]);
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
	
	public void sendMessage(String reply) {
				try {
					OutputStream output= this._socket.getOutputStream();
					DataOutputStream objectOutputStream = new DataOutputStream(output);
					objectOutputStream.writeUTF(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  
	}

	public long getReceievedCount() {
		return _receievedCount;
	}
}
