package gash.socket.common;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gash.comm.extra.Message;
import gash.comm.extra.XmlBuilder;
import gash.comm.payload.MessageBuilder.MessageType;
import junit.framework.Assert;

public class XmlBuilderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testMessageEncoding() throws Exception {
		Message data = new Message();
		data.setSource("test-src");
		data.setType(MessageType.join);
		data.setPayload("This is a test");

		// to xml
		String out = XmlBuilder.encode(data);
		Assert.assertNotNull(out);
		System.out.println("XML: " + out);

		// to object
		Message data2 = XmlBuilder.decode(out);
		System.out.println("Object: " + data2);
	}

}
