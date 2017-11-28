package com.gdzie.znajde.client;

import java.io.*;
import javax.net.ssl.SSLSocket;

class Client implements Runnable {
	Client(SSLSocket socket){
		this.socket = socket;
	}
	private SSLSocket socket;
	private InputStream inputStream;
	private ObjectInputStream oInputStream;
	private ObjectOutputStream oos;
	private OutputStream os;
	private String msg;
	private Object msgObj;
	
	public void run() {
		try{
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			inputStream = socket.getInputStream();
			oInputStream = new ObjectInputStream(inputStream);
	
			/*while(true){
				msgObj = oInputStream.readObject();
				msg = msgObj instanceof String ? (String)msgObj : null;
				switch(msg){
					case "download": FileManagement.downloadFile("C:\\Users\\ZWAC0_000\\Desktop\\GIT\\PC-Control\\src\\com\\gdzie\\znajde\\client\\TO.pdf", socket);
					break;
				}
			}*/
			oos.writeObject("unmute");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}