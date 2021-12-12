package gash.app.producer;

public class Topic {
	private String topicName;
	
	public Topic(String name) {
		this.topicName = name;
	}
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
