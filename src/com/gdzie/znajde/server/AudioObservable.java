package com.gdzie.znajde.server;

import java.io.ByteArrayOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioObservable extends Observable implements Runnable {

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
	
	public void deleteObserver(Observer o) {
		super.deleteObserver(o);
		
		if((countObservers() == 0) && (line.isOpen())) {
			line.flush();
			line.close();
		}
	}
}
