package gash.app.client;


import gash.comm.extra.Message;
import gash.comm.payload.MessageBuilder.MessageType;

/**
 * 
 * @author gash
 * 
 */
public class HeartBeat extends Thread {
	private boolean _forever = true;
	private long _interval;
	private long _idleTime;
	private BasicClient _bc;
	
	/**
	 * create a new monitor
	 * 
	 * @param interval
	 *            long how often to check
	 * @param idleness
	 *            long what is considered idle
	 */
	public HeartBeat(BasicClient bc, long interval, long idleness) {
		this._interval = interval;
		this._idleTime = idleness;
		this._bc = bc;
	}

	/**
	 * stop monitoring on the next _interval
	 */
	public void stopMonitoring() {
		_forever = false;
	}

	/**
	 * ran in the thread to monitor for idle threads
	 */
	public void run() {
		while (_forever) {
			try {
				long idle = System.currentTimeMillis() - _idleTime;
				Thread.sleep(_interval);
				String msg1 = "Still Alive?";
				Message msg  = new Message();
				msg.setPayload(msg1);
				msg.setType(MessageType.heartBeat);
				_bc.sendMessage(msg);
				if (!_forever) {
					System.out.println("\nGoodbye");
					_bc.stopSession();
					break;
				}

			} catch (Exception e) {
				stopMonitoring();
				System.out.println("Exiting Hearbeat");
			}
		}
	}
} // class MonitorSessions

