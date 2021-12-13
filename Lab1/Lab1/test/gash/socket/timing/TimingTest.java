//package gash.socket.timing;
//
//import java.util.Properties;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import gash.app.client.BasicClient;
//import gash.comm.core.BasicSocketServer;
//import gash.comm.core.Settings;
//import gash.comm.extra.Message;
//import gash.comm.payload.MessageBuilder.MessageType;
//
//public class TimingTest {
//	private BasicClient bc;
//
//	@Before
//	public void setUp() throws Exception {
//		Properties p = new Properties();
//		p.setProperty(Settings.PropertyHost, "127.0.0.1");
//		p.setProperty(Settings.PropertyPort, "2100");
//
//		long st = System.currentTimeMillis();
//		bc = new BasicClient(p);
//		bc.startSession();
//		bc.setName("timing-test");
//		bc.join("timing");
//		long et = System.currentTimeMillis();
//
//		System.out.println("Setup time: " + (et - st) + " msec");
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		bc.stopSession();
//	}
//
//	@Test
//	public void testTiming() {
//		long cnt = 0L;
//		Message m = new Message();
//		m.setType(MessageType.msg);
//		m.setPayload("timing test");
//		m.setSource(this.getClass().getName());
//
//		long st = System.currentTimeMillis();
//		final int Count = 100;
//		for (int n = 0; n < Count; n++) {
//			m.setMid(Long.toString(cnt++));
//			bc.sendMessage(m);
//		}
//
//
//		try {
//			while (bc.isPendingResponses()) {
//				System.out.println("** Waiting for responses **");
//				Thread.sleep(100);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		long et = System.currentTimeMillis();
//
//		System.out.println("\nTotal time: " + (et - st) + " msec");
//		System.out.println("Ave time/msg: " + (float) (et - st) / Count + " msec");
//	}
//
//}
