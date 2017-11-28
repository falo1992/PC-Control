package com.gdzie.znajde.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class VideoManagement {
	//TODO: Przeniesc do singletona, stworzyc interface dla wszystkich metod
	private static List<Webcam> webcam = Webcam.getWebcams();
	
	public static List<String> getWebcamList() {
		List<String> camList = new ArrayList<String>();
		
		for(Webcam cam : webcam) {
			camList.add(cam.getName());
		}

		return camList;
	}
	
	public static void sendVideo(Socket socket, ObjectOutputStream oos) {
		webcam.get(0).open();
		
		try{			
			while(true) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(webcam.get(0).getImage(), "png", baos);
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				oos.writeObject(imageInByte);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		webcam.get(0).close();
	}
}
