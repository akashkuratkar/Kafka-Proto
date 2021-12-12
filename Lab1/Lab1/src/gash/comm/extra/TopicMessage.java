package gash.comm.extra;

public class TopicMessage {
	private String topicName;
	private String message;
	private String messageID;
	public TopicMessage(String topic, String message, String mid) {
		this.topicName = topic;
		this.message = message;
		this.messageID = mid;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "TopicMessage [topicName=" + topicName + ", message=" + message + ", messageID=" + messageID + "]";
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	
}
