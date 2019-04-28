package com.olaf.majer.server;


import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

public class Server implements Runnable {
	private final static Logger logger = Logger.getLogger(Server.class);
	
	public static volatile boolean isMute = false;
	
	Server(){
		serverPort = 2000;
		clientThread = new ClientThread[max];
	}
	
	private static AudioObservable aov;
	private static VideoObservable vov;
	private SSLServerSocketFactory serverSocketFactory;
	private SSLSocket socket;
	private SSLServerSocket serverSocket;
	private SSLServerSocket audioServerSocket;
	private SSLServerSocket videoServerSocket;
	private SSLServerSocket heartbeatServerSocket;
	private ClientThread clientThread[];
	private int serverPort;
	private static int max = 20;
	
	public void run(){
		EventQueue.invokeLater(new Runnable() {	
			public void run() {
				new ServerFrame();
			}
		});
		
		KeyStore keyStore;
		
        try {
            keyStore = KeyStore.getInstance("JKS");
            log("Initializing KeyStore");
            InputStream keystoreStream = ClassLoader.getSystemResourceAsStream("serverKeyStore");
            keyStore.load(keystoreStream, "changeit".toCharArray());
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keyStore, "changeit".toCharArray());
            KeyManager[] keymanagers =  kmfactory.getKeyManagers();

            TrustManagerFactory tmf=TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(keyStore);

            SSLContext sslContext=SSLContext.getInstance("TLS");

            sslContext.init(keymanagers, tmf.getTrustManagers(), new SecureRandom());
            log("Setting SSL context");

            serverSocketFactory=sslContext.getServerSocketFactory();
			log("Starting server");
            
			serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(serverPort);
			audioServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(3000);
			videoServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(4000);
			heartbeatServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(5000);
			log("Server listening on port " + serverPort);

        }catch(Exception e){
        	e.printStackTrace();
        }
        
        aov = new AudioObservable();
        vov = new VideoObservable();
        
        new Thread(aov).start();
        new Thread(vov).start();
        
        ObjectInputStream OIS = null;
        ObjectOutputStream OOS = null;
        
		while(true){
			try{
				socket = (SSLSocket) serverSocket.accept();
				OIS = new ObjectInputStream(socket.getInputStream());
				OOS = new ObjectOutputStream(socket.getOutputStream());
				log("Client connected from " + socket.getInetAddress());
				
				for(int i=0;i<max;i++){
					if(clientThread[i] == null){
						clientThread[i] = new ClientThread(OIS, OOS, socket.getInputStream(), socket.getOutputStream(), audioServerSocket, videoServerSocket, socket.getInetAddress().getHostName());
						clientThread[i].start();
						break;
					}
				}
				
				SSLSocket socket = (SSLSocket) heartbeatServerSocket.accept();
				InputStream heartbeatIS = socket.getInputStream();
				@SuppressWarnings("unused")
				ObjectInputStream heartbeatOIS = new ObjectInputStream(heartbeatIS);
				OutputStream heartbeatOS = socket.getOutputStream();
				@SuppressWarnings("unused")
				ObjectOutputStream heartbeatOOS = new ObjectOutputStream(heartbeatOS);
				System.out.println("Heartbeat connected");
				
			}catch(SocketException e){
				log(e.getMessage());
			}catch(IOException e){
				log(e.getMessage());
			}
		}
	}
	
	public static AudioObservable getAov() {
		return aov;
	}

	public static VideoObservable getVov() {
		return vov;
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
