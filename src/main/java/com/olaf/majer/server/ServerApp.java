package com.olaf.majer.server;

/**
 * Startup class.
 * 
 * @author Olaf Majer
 *
 */
public class ServerApp {
	private static Server server = new Server();
	
	public static void main(String[] args){
		VolumeManagement.setVolumeUnmute();
		server.run();
	}
}
