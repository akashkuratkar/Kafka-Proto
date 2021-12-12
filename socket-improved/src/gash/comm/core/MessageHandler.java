package gash.comm.core;


public interface MessageHandler {
	void process(byte[] msg);
}
