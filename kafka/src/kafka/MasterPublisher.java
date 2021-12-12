package kafka;

import java.util.List;

import kafka.comm.models.Subscribe;

public class MasterPublisher {
	
	private MasterService masterService;
	
	public MasterPublisher(){
		this.masterService = new MasterService();
	}
	

	public void fan_out(String topic_name) {
		List<Subscribe>  subscribers  = MasterConfig.topic_list.get(topic_name);
		TopicMessage message = new TopicMessage();
		for(Subscribe sub: subscribers) {
			int currOffset = sub.getOffset();
			/*String msg = masterService.read_message(topic_name, sub.getOffset());
			while(msg!=null) {
				message.setTopic_name(topic_name);
				message.setMessageString(msg);
				if(masterService.sendMessage(sub.getSubscribe_id(),message)) {
					currOffset++;
				}
				//msg = masterService.read_message(topic_name, sub.getOffset());
				
			}*/
			
		}
		
	}
	
	public List<String> on(String topic_name,String subscriberName) {
		List<Subscribe>  subscribers  = MasterConfig.topic_list.get(topic_name);
		Subscribe subscriber = null;
		for(Subscribe sub: subscribers) {
			subscriber = sub;
		}
		return masterService.read_message(topic_name, subscriber);
	}
}
