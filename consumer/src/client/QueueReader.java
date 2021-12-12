package client;

public class QueueReader extends Thread{
	private static boolean _forever = true;
	
	void stopForever() {
		_forever = false;
	}
	@Override
	public void run() {
		while(_forever) {
			if(MasterQueue.pollFromQueue() == -1) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//if there is nothing on queue, sleep for 5 sec
			}
		}
	}
}
