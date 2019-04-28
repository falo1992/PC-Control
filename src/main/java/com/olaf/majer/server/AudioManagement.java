package com.olaf.majer.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class used to send audio to client.
 * 
 * @author Olaf Majer
 *
 */
public class AudioManagement implements Observer {
	private final static Logger logger = Logger.getLogger(AudioManagement.class);
	
	private AudioObservable aov = null;
	private ObjectOutputStream oos;
	
	/**
	 * Creates observer for audio.
	 * 
	 * @param oos ObjectOutputStream used to send audio to client.
	 */
	public AudioManagement(ObjectOutputStream oos) {
		this.aov = Server.getAov();
		aov.addObserver(this);
		this.oos = oos;
	}

	/**
	 * Sends audio to clent.
	 * 
	 * @param buffer Buffer with audio.
	 */
	private void sendAudio(byte[] buffer) {
		try {
			oos.writeObject(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			aov.deleteObserver(this);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(aov == o) {
			sendAudio((byte[]) arg);
		}
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
