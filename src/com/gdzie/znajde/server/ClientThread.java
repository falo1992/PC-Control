package com.gdzie.znajde.server;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket socket;
	private int i;
	
	ClientThread(Socket socket, int i){
		this.socket = socket;
		this.i = i;
	}
	@Override
	public void run(){

	}
}
