package gash.app.producer;

import java.util.Properties;

import gash.app.client.BasicClient;
import gash.app.client.ConsoleApp;
import gash.comm.extra.TopicMessage;

public class Producer {
	private Topic topic;
	//private static BasicClient bc;
	//public Producer()
	public Topic createTopic(String name) {
		this.topic = new Topic(name);
		
		//bc = new BasicClient(ca.get_setup());
		//ca.run();
		return this.topic;
	}
	
	public boolean sendMessage(BasicClient bc,Topic topic) {
		bc.sendMessage("topic",topic.getTopicName());
		return false;
	}
	
	public boolean sendMessage(BasicClient bc, TopicMessage tm) {
		bc.sendMessage(tm);
		return false;
	}
}
