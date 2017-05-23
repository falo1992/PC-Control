package com.gdzie.znajde.client;

import java.net.Socket;

public class ClientApp {
	private static Socket socket;
	
	public static void main(String[] args){
		try{
			socket = new Socket("localhost",2000);
		}catch(Exception e){
			System.err.println(e);
		}
	}
}
