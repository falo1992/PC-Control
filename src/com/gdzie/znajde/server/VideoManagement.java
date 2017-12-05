package com.gdzie.znajde.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.github.sarxos.webcam.Webcam;

public class VideoManagement implements Observer {

	private static List<Webcam> webcam = Webcam.getWebcams();
	private ObjectOutputStream oos;
	private VideoObservable vov = null;
	
	public VideoManagement(ObjectOutputStream oos) {
		this.vov = Server.getVov();
		vov.addObserver(this);
		this.oos = oos;
	}
	
	public static List<String> getWebcamList() {
		List<String> camList = new ArrayList<String>();
		
		for(Webcam cam : webcam) {
			camList.add(cam.getName());
		}

		return camList;
	}

//	@Override
//	public void run() {
//		webcam.get(0).open();
//		
//		ClientThread.videoPlay = true;
//		
//		try{			
//			while(ClientThread.videoPlay) {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				ImageIO.write(webcam.get(0).getImage(), "png", baos);
//				byte[] imageInByte = baos.toByteArray();
//				baos.close();
//				oos.writeObject(imageInByte);
//			}
//			
//			oos.close();
//		} catch(IOException e) {
//			e.printStackTrace();
//		} finally {
//			ClientThread.videoPlay = false;
//			try{
//				oos.close();
//			}catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		webcam.get(0).close();
//	}
	
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
}
