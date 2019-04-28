package com.olaf.majer.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class responsible for handling client requests.
 * 
 * @author Olaf Majer
 *
 */
public class ClientThread extends Thread {
	private final static Logger logger = Logger.getLogger(ClientThread.class);
	
	private AudioManagement audioManager = null;
	private VideoManagement videoMenager = null;
	private static SSLSocket audioSocket;
	private static SSLSocket videoSocket;
	private static SSLServerSocket audioServerSocket;
	private static SSLServerSocket videoServerSocket;
	private ObjectOutputStream oos = null;
	private ObjectOutputStream audioOOS = null;
	private OutputStream audioOS = null;
	private ObjectOutputStream videoOOS = null;
	private OutputStream videoOS = null;
	private OutputStream os = null;
	private InputStream is = null;
	private ObjectInputStream ois = null;
	private String msg;
	private Object msgObj;
	private String address;
	
	/**
	 * Creates thread for client.
	 * 
	 * @param ois ObjectInputStream created for communication with client.
	 * @param oos ObjectOutputStream created for communication with client.
	 * @param is InputStream created for communication with client.
	 * @param os OutputStream created for communication with client.
	 * @param audioSocket Socket created for sending audio.
	 * @param videoSocket Socket created for sending video.
	 * @param address Client address.
	 */
	ClientThread(ObjectInputStream ois, ObjectOutputStream oos, InputStream is, OutputStream os, SSLServerSocket audioSocket, SSLServerSocket videoSocket, String address){
		ClientThread.audioServerSocket = audioSocket;
		ClientThread.videoServerSocket = videoSocket;
		this.address = address;
		this.is = is;
		this.os = os;
		this.ois = ois;
		this.oos = oos;
	}
	
	@Override
	public void run(){
		try{
			String filePath;
			
			while(ois != null){
				msgObj = ois.readObject();
				msg = msgObj instanceof String ? (String) msgObj : null;
				System.out.println(msg);

				switch (msg) {
				case "get folder":
					msgObj = ois.readObject();
					filePath = msgObj instanceof String ? (String) msgObj : null;
					
					if (filePath.equals("DESKTOP")) {
						oos.writeObject(FileManagement.listFolder(FileManagement.DESKTOP));
						
					} else if (filePath.equals("DOCUMENTS")) {
						oos.writeObject(FileManagement.listFolder(FileManagement.DOCUMENTS));
						
					} else if (filePath.equals("DOWNLOADS")) {
						oos.writeObject(FileManagement.listFolder(FileManagement.DOWNLOADS));
						
					} else {
						oos.writeObject(FileManagement.listFolder(filePath));
					}
					break;

				case "download":
					msgObj = ois.readObject();
					filePath = msgObj instanceof String ? (String) msgObj : null;
					FileManagement.uploadFile(filePath, oos, os);
					break;

				case "upload":
					{
						msgObj = ois.readObject();
						filePath = msgObj instanceof String ? (String) msgObj : null;
						
						msgObj = ois.readObject();
						String fileName = msgObj instanceof String ? (String) msgObj : null;
						
						if (filePath.equals("DESKTOP")) {
							filePath = FileManagement.DESKTOP + fileName;
							
						} else if (filePath.equals("DOCUMENTS")) {
							filePath = FileManagement.DOCUMENTS + fileName;
							
						} else if (filePath.equals("DOWNLOADS")) {
							filePath = FileManagement.DOWNLOADS + fileName;
						}
						
						FileManagement.downloadFile(filePath, is);
					}
					break;

				case "shutdown":
					if (PowerManagement.forceShutdown()) {
						log("Shutdown succeeded");
						
					} else {
						log("Shutdown failed");
					}
					break;

				case "restart":
					if (PowerManagement.forceRestart()) {
						log("Restart succeeded");
						
					} else {
						log("Restart failed");
					}
					break;

				case "force shutdown":
					if (PowerManagement.forceShutdown()) {
						log("Force shutdown succeeded");
						
					} else {
						log("Force shutdown failed");
					}
					break;

				case "logout":
					if (PowerManagement.logout()) {
						log("Logout succeeded");
						
					} else {
						log("Logout failed");
					}
					break;

				case "volume up":
					if (VolumeManagement.setVolumeUp()) {
						log("Volume up succeeded");
						log("Current volume: " + VolumeManagement.getVolume());
						
					} else {
						log("Volume up failed");
						log("Current volume: " + VolumeManagement.getVolume());
					}
					break;

				case "volume down":
					if (VolumeManagement.setVolumeDown()) {
						log("Volume down succeeded");
						log("Current volume: " + VolumeManagement.getVolume());
						
					} else {
						log("Volume down failed");
						log("Current volume: " + VolumeManagement.getVolume());
					}
					break;

				case "mute":
					if (VolumeManagement.setVolumeMute()) {
						log("Mute succeeded");

					} else {
						log("Mute failed");
					}
					break;

				case "unmute":
					if (VolumeManagement.setVolumeUnmute()) {
						log("Unmute succeeded");

					} else {
						log("Unmute failed");
					}
					break;

				case "set volume":
					msgObj = ois.readObject();
					double value = msgObj instanceof Double ? (Double) msgObj : 0;
					if (VolumeManagement.setVolume(value)) {
						log("Set volume succeeded");
						log("Current volume: " + VolumeManagement.getVolume());
						
					} else {
						log("Set volume failed");
						log("Current volume: " + VolumeManagement.getVolume());
					}
					break;

				case "video":
					if (videoMenager == null) {
						startVideoSocket();
						videoMenager = new VideoManagement(videoOOS);
						
					} else {
						Server.getVov().addObserver(videoMenager);
					}
					break;

				case "stop video":
					Server.getVov().deleteObserver(videoMenager);
					break;

				case "audio":
					if (audioManager == null) {
						startAudioSocket();
						audioManager = new AudioManagement(audioOOS);
						
					} else {
						Server.getAov().addObserver(audioManager);
					}
					break;

				case "stop audio":
					Server.getAov().deleteObserver(audioManager);
					break;

				case "audio video":
					break;

				case "get mute":
					oos.writeObject(VolumeManagement.getMute());
					break;

				case "get volume":
					oos.writeObject(VolumeManagement.getVolume());
					break;

				case "open player":
					msgObj = ois.readObject();
					String wmpFilePath = msgObj instanceof String ? (String) msgObj : "";
					WindowsMediaPlayerManagement.openWMP(wmpFilePath);
					break;

				case "next":
					WindowsMediaPlayerManagement.next();
					break;

				case "previous":
					WindowsMediaPlayerManagement.previous();
					break;

				case "fullscreen":
					WindowsMediaPlayerManagement.fullscreen();
					break;

				case "wmp mute":
					WindowsMediaPlayerManagement.mute();
					break;

				case "wmp v_up":
					WindowsMediaPlayerManagement.volumeUp();
					break;

				case "wmp v_down":
					WindowsMediaPlayerManagement.volumeDown();
					break;

				case "wmp shuffle":
					WindowsMediaPlayerManagement.shuffle();
					break;

				case "wmp repeat":
					WindowsMediaPlayerManagement.repeat();
					break;

				case "wmp play":
					WindowsMediaPlayerManagement.playPause();
					break;

				case "wmp stop":
					WindowsMediaPlayerManagement.stop();
					break;

				case "wmp library":
					WindowsMediaPlayerManagement.libraryView();
					break;

				case "wmp player":
					WindowsMediaPlayerManagement.playerView();
					break;
					
				case "wmp close":
					WindowsMediaPlayerManagement.close();
				}
			}
		}catch(ClassNotFoundException | IOException e) {
			// Do nothing
		} finally {
			log("Client " + address + " disconnected");
		}
	}
	
	/**
	 * Connects audio socket.
	 */
	private void startAudioSocket() {
		try{
			audioSocket = (SSLSocket) audioServerSocket.accept();
			audioOS = audioSocket.getOutputStream();
			audioOOS = new ObjectOutputStream(audioOS);
			log("Audio connected successfully for " + address);
			
		}catch(IOException e) {
			log("Audio connection unsuccessfull for " + address);
		}
	}
	
	/**
	 * Connects video socket.
	 */
	private void startVideoSocket() {
		try{
			videoSocket = (SSLSocket) videoServerSocket.accept();
			videoOS = videoSocket.getOutputStream();
			videoOOS = new ObjectOutputStream(videoOS);
			log("Video connected successfully for " + address);
			
		}catch(IOException e) {
			log("Video connection unsuccessfull for " + address);
		}
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
