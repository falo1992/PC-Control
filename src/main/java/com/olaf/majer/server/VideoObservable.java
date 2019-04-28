package com.olaf.majer.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.github.sarxos.webcam.Webcam;
import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class responsible for updating video for clients.
 * 
 * @author Olaf Majer
 *
 */
public class VideoObservable extends Observable implements Runnable {
	private final static Logger logger = Logger.getLogger(VideoObservable.class);

	private boolean camOpen = false;
	private static List<Webcam> webcam = Webcam.getWebcams();
	
	@Override
	public void run() {
		while(true) {
			try{	
				if((countObservers() > 0) && (camOpen)) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(webcam.get(0).getImage(), "png", baos);
					byte[] imageInByte = baos.toByteArray();
					baos.close();
					setChanged();
					notifyObservers(imageInByte);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		
		if(!webcam.get(0).isOpen()) {
			webcam.get(0).open();
			while(webcam.get(0).getImage() == null) {}
			camOpen = true;
		}
	}
	
	@Override
	public void deleteObserver(Observer o) {
		super.deleteObserver(o);
		
		if((countObservers() == 0) && (webcam.get(0).isOpen())) {
			webcam.get(0).close();
			camOpen = false;
		}
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
