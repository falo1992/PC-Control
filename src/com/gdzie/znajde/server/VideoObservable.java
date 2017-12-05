package com.gdzie.znajde.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class VideoObservable extends Observable implements Runnable {

	private boolean camOpen = false;
	private static List<Webcam> webcam = Webcam.getWebcams();
	
	public static List<String> getWebcamList() {
		List<String> camList = new ArrayList<String>();
		
		for(Webcam cam : webcam) {
			camList.add(cam.getName());
		}

		return camList;
	}
	
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

	public void addObserver(Observer o) {
		super.addObserver(o);
		
		if(!webcam.get(0).isOpen()) {
			webcam.get(0).open();
			while(webcam.get(0).getImage() == null) {}
			camOpen = true;
		}
	}
	
	public void deleteObserver(Observer o) {
		super.deleteObserver(o);
		
		if((countObservers() == 0) && (webcam.get(0).isOpen())) {
			webcam.get(0).close();
			camOpen = false;
		}
	}
	
}
