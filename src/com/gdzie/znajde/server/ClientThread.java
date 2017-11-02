package com.gdzie.znajde.server;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import com.gdzie.znajde.server.gui.ServerFrame;

public class ClientThread extends Thread {
	private static SSLSocket socket;
	private int i;
	private ObjectOutputStream oos = null;
	private OutputStream os = null;
	private InputStream is = null;
	private ObjectInputStream ois = null;
	private String msg;
	private Object msgObj;
	
	ClientThread(SSLSocket socket, int i){
		ClientThread.socket = socket;
		this.i = i;
	}
	
	public static Socket getSocket(){
		return socket;
	}
	
	@Override
	public void run(){
		try{
			String filePath;
			os = socket.getOutputStream();
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			oos = new ObjectOutputStream(os);
			
			while(true){
				msgObj = ois.readObject();
				msg = msgObj instanceof String ? (String) msgObj : null;
				switch(msg) {
					case "download":    msgObj = ois.readObject();
									    filePath = msgObj instanceof String ? (String) msgObj : null;
									    if(FileManagement.uploadFile(filePath, socket)) {
									    	ServerFrame.log("Upload completed");
									    }else{
									    	ServerFrame.log("Upload failed");
									    }
									    break;
					case "upload":      msgObj = ois.readObject();
										filePath = msgObj instanceof String ? (String) msgObj : null;
										if(FileManagement.downloadFile(filePath, socket)) {
											ServerFrame.log("Download completed");
										}else{
											ServerFrame.log("Download failed");
										}
										break;
					case "shutdown":    if(PowerManagement.shutdown()) {
											ServerFrame.log("Shutdown succeeded");
										}else{
											ServerFrame.log("Shutdown failed");
										}
									    break;
					case "restart":     if(PowerManagement.restart()) {
											ServerFrame.log("Restart succeeded");
										}else{
											ServerFrame.log("Restart failed");
										}
									    break;
					case "volume up":   if (VolumeManagement.setVolumeUp()) {
											ServerFrame.log("Volume up succeeded");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}else{
											ServerFrame.log("Volume up failed");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}
										break;
					case "volume down": if(VolumeManagement.setVolumeDown()) {
											ServerFrame.log("Volume down succeeded");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}else{
											ServerFrame.log("Volume down failed");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}
										break;
					case "mute":		if(VolumeManagement.setVolumeMute()) {
											ServerFrame.log("Mute succeeded");
											VolumeManagement.setMute();
										}else{
											ServerFrame.log("Mute failed");
										}
										break;
					case "unmute":      if(VolumeManagement.setVolumeUnmute()) {
											ServerFrame.log("Unmute succeeded");
											VolumeManagement.setMute();
										}else{
											ServerFrame.log("Unmute failed");
										}
										break;
					case "set volume":  msgObj = ois.readObject();
										double value = msgObj instanceof Double ? (Double) msgObj : 0;
										if(VolumeManagement.setVolume(value)) {
											ServerFrame.log("Set volume succeeded");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}else{
											ServerFrame.log("Set volume failed");
											ServerFrame.log("Current volume: " + VolumeManagement.getVolume());
										}
										break;
					case "video":		
										break;
					case "audio":		
										break;
					case "audio video":
										break;
					case "get mute":	oos.writeObject(VolumeManagement.getMute());
										break;
					case "get volume":	oos.writeObject(VolumeManagement.getVolume());
										break;
				}
			}
		}catch(SSLException e) {
			ServerFrame.log(e.getMessage());
		}catch(IOException e) {
			ServerFrame.log(e.getMessage());
		}catch(ClassNotFoundException e) {
			ServerFrame.log(e.getMessage());
		}
	}
}
