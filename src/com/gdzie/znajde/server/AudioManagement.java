package com.gdzie.znajde.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.TargetDataLine;

public class AudioManagement implements Observer {
	
	private AudioObservable aov = null;
	private ObjectOutputStream oos;
	
	public AudioManagement(ObjectOutputStream oos) {
		this.aov = Server.getAov();
		aov.addObserver(this);
		this.oos = oos;
	}

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
}
