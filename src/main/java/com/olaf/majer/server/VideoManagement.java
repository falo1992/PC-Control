package com.olaf.majer.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class used to send video to client.
 * 
 * @author Olaf Majer
 *
 */
public class VideoManagement implements Observer {
	private final static Logger logger = Logger.getLogger(VideoManagement.class);

	private ObjectOutputStream oos;
	private VideoObservable vov = null;
	
	/**
	 * Creates observer for webcam video.
	 * 
	 * @param oos ObjectOutputStream used to send video to client.
	 */
	public VideoManagement(ObjectOutputStream oos) {
		this.vov = Server.getVov();
		vov.addObserver(this);
		this.oos = oos;
	}
	
	/**
	 * Send video image to client.
	 * 
	 * @param buffer Buffer holding single frame from webcam.
	 */
	private void sendVideo(byte[] buffer) {
		try {
			oos.writeObject(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			vov.deleteObserver(this);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(vov == o) {
			sendVideo((byte[])arg);
		}
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
