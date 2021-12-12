package client;

import java.util.LinkedList;
import java.util.Queue;

public class MasterQueue {
	public static Queue<String[]> masterQueue = new LinkedList<String[]>();
	private static int size = 10000;
	
	public static void addToQueue(String[] val) {
		if(masterQueue.size()==size) {
			System.out.println("Cannot add to the queue, size exceeded!");
			return;
		}
		masterQueue.add(val);
	}
	public static int pollFromQueue() {
		if(masterQueue.size()==0) 
			return -1;
		String[] val = masterQueue.poll();
		System.out.println("Topic-> "+val[0]);
		System.out.println("Msg-> "+val[1]);
		return 1;
	
	}

}
