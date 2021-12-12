package kafka.comm.extra;

import java.util.Date;

import kafka.comm.payload.MessageBuilder.MessageType;

public class Message {
	// header info
	private MessageType type;
	private String mid;
	private String source;
	//private Date sent;
	private Date received;
	private String Status;
	private String topicName;
	private String payload;
	
	public String getStatus() {
		return Status;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String body) {
		this.payload = body;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.source).append("(").append(this.type).append(",").append(this.mid).append("): ")
				.append(this.payload);
		return sb.toString();
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
}
