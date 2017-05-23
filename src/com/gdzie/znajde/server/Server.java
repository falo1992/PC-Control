package com.gdzie.znajde.server;


/*import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
*/
import java.net.Socket;
import java.net.ServerSocket;
import com.gdzie.znajde.server.ClientThread;

public class Server implements Runnable {
	private Socket socket;
	private ServerSocket serverSocket;
	private ClientThread clientThread[];
	private int serverPort;
	private static int max = 20;
	Server(){
		serverPort = 2000;
		clientThread = new ClientThread[max];
	}
	public void run(){
		try{
			serverSocket = new ServerSocket(serverPort);
		}catch(Exception e){
			System.err.println(e);
		}
		while(true){
			try{
				System.out.println("czeka");
				socket = serverSocket.accept();
				for(int i=0;i<max;i++){
					if(clientThread[i]==null){
						clientThread[i] = new ClientThread(socket,i);
						clientThread[i].start();
						break;
					}
				}
			}catch(Exception e){
				System.err.println(e);
			}
		}
	}
}
