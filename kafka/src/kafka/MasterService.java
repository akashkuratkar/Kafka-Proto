package kafka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kafka.comm.models.Subscribe;

public class MasterService {
	private final Integer _mutex;
	BufferedReader buffReader;
	//private final MasterPublisher masterPublisher = new MasterPublisher();
	
	public MasterService() {
		_mutex = Integer.valueOf(1);
		//MasterConfig.topic_list.put("Ali",new ArrayList<>());
	}

	public String create_topic(String topic_name) {
		synchronized (_mutex) {
			File f = new File(topic_name + ".csv");
			if (f.exists() && !f.isDirectory() && MasterConfig.topic_list.containsKey(topic_name)) {
				return "Topic already exist";
			} else {
					MasterConfig.topic_list.put(topic_name,new ArrayList<Subscribe>());
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return "Topic created successfully!";
	}
	
	public String substribe_topic(String topic_name,String consumer_name) {
		synchronized (_mutex) {
			File f = new File(topic_name + ".csv");
			if (f.exists() && !f.isDirectory() && MasterConfig.topic_list.containsKey(topic_name)) {
				List<Subscribe> subsribers = MasterConfig.topic_list.get(topic_name);
				subsribers.add(new Subscribe(consumer_name,0));
				return "Topic Subscribed Successfully";
			}
		}
		return "Topic does not exist!";
	}
	
	public String write_message(TopicMessage message) {
		synchronized (_mutex) {
			File newFile = new File(message.getTopic_name() + ".csv");
			if (newFile.exists() && !newFile.isDirectory()) {
				try {
					// CsvWriter csvOutput = new CsvWriter(new FileWriter(newFile, true), ',');
					BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
					writer.write(message.getMessageString());
					writer.newLine();

					writer.flush();
					writer.close();
					//masterPublisher.fan_out(message.topic_name);
					return "Message added succssfully!";
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				return "Invalid topic name";
			}
		}
		return "Error in writing message";
	}

	public boolean sendMessage(String subId,TopicMessage message) {
		return true;
	}
	
	public List<String> read_message(String topic_name,Subscribe sub) {
		synchronized (_mutex) {
				try {
					var path =topic_name + ".csv";
					getCsvReader(path);
					return read(sub);
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return null;
	}
	
	public List<String> read(Subscribe sub) {
		int counter =  1;
		List<String> messages = new ArrayList<>();

		try {
			while(true) {

			String line = this.buffReader.readLine();

			if (line != null) {
				if(counter>=sub.getOffset()) {
					messages.add(line);
				}
				counter++;
			
			} else {

				break;

			}
			}
			sub.setOffset(counter);
			return messages;

		} catch (IOException e) {

			e.printStackTrace();

			return null;

		}

	}
	
	private void getCsvReader(String path) throws FileNotFoundException {
		try {
			File file = new File(path);

			FileReader fr = new FileReader(file);

			this.buffReader = new BufferedReader(fr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		MasterService ms = new MasterService();
		// Message message = new Message();
		// message.setTopic_name("Test");
		// message.setMessageId("2321313");
		// message.setMessageString("Akash");
		// ms.write_message(message);
		System.out.println(ms.create_topic("Test"));
	}
}
