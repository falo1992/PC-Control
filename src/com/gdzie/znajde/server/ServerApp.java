package com.gdzie.znajde.server;

public class ServerApp {
	private static Server server = new Server();
	
	public static void main(String[] args){
		VolumeManagement.extractProgram();
		server.run();
	}
}
