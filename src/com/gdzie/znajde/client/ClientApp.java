package com.gdzie.znajde.client;

import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.net.*;
import javax.net.ssl.*;

public class ClientApp {
	
	private static SocketFactory socketFactory;
	private static SSLSocket socket;
	private static String socketHost = "localhost";
	private static int socketPort = 2000;
	private static ArrayList<String> availableAddresses = new ArrayList<String>();
	
	Client client;
	
	public static void main(String[] args){
		try{
			new Thread(new Client(socket)).start();
		}catch(Exception e){
			System.err.println(e);
		}
	}
	
	public static boolean connect(SocketAddress socketAddress){
		try{
			setTrustStore();
			socket = (SSLSocket) socketFactory.createSocket(socketHost, socketPort);
			return true;
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setTrustStore(){
		System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\ZWAC0_000\\eclipse\\jee-neon\\eclipse\\jre\\lib\\security\\serverStore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		socketFactory = SSLSocketFactory.getDefault();
	}
	
	public static boolean checkIfRunning(SocketAddress socketAddress){
		try{
			Socket testSocket = new Socket();
			testSocket.connect(socketAddress, 50);
			testSocket.close();
			return true;
		}catch(IOException e){
		}
		return false;
	}
	
	public static void checkForDevices() throws IOException {
		ArrayList<String> hostAddresses = new ArrayList<String>();
		String subnet;
		int start;
		int end;
		InetAddress inet = InetAddress.getLocalHost();
		InetAddress[] allHostAddresses = InetAddress.getAllByName(inet.getCanonicalHostName());
		for (InetAddress address: allHostAddresses) {
			if(address instanceof Inet4Address){
				hostAddresses.add(address.toString());
			}
		}
		for(String hostAddress: hostAddresses){
			start = hostAddress.indexOf("/");
			end = hostAddress.lastIndexOf(".");
			subnet = hostAddress.substring(start + 1, end);	
			for(int i=1;i<255;i++){
				String host = subnet+"."+i;
				if(checkIfRunning(new InetSocketAddress(host, 2000))){
					availableAddresses.add(host);
					System.out.println(host+" jest dostêpny.");
				}
			}
		}
		System.out.println("Sprawdzono");
	}
	
	public static void close(){
		try{
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}