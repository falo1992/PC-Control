package com.olaf.majer.server;

import java.io.ByteArrayOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class responsible for updating audio for clients.
 * 
 * @author Olaf Majer
 *
 */
public class AudioObservable extends Observable implements Runnable {
	private final static Logger logger = Logger.getLogger(AudioObservable.class);

	private int bufferSize = 512;
	private AudioFormat format = null;
	private TargetDataLine line = null;
	
	@Override
	public void run() {
		format = new AudioFormat(44100.0f, 16, 1, true, false);
		
		try{
			line = AudioSystem.getTargetDataLine(format);
			byte buffer[] = new byte[bufferSize];
			while(true) {
				if((countObservers() > 0) && (line.isOpen())) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int count = line.read(buffer, 0, buffer.length);
					if(count > 0) {
						baos.write(buffer, 0, count);
						setChanged();
						notifyObservers(baos.toByteArray());
					}
				}
			}
		}catch(LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		
		if(!line.isOpen()) {
			try {
				line.open(format);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
			line.start();
		}
	}
	
	@Override
	public void deleteObserver(Observer o) {
		super.deleteObserver(o);
		
		if((countObservers() == 0) && (line.isOpen())) {
			line.flush();
			line.close();
		}
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
