package battlechallenge.poc;

import java.io.*;
import java.net.*;
import java.util.*;

public class MyObjectServer extends Thread {

	private ServerSocket dateServer;

	public static void main(String argv[]) throws Exception {
		new MyObjectServer();
	}

	public MyObjectServer() throws Exception {
		dateServer = new ServerSocket(3000);
		System.out.println("Server listening on port 3000.");
		this.start();
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for connections.");
				Socket client = dateServer.accept();
				System.out.println("Accepted a connection from: "
						+ client.getInetAddress());
				new Connect(client);
			} catch (Exception e) {
			}
		}
	}
}

class Connect extends Thread {
	private Socket client = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public Connect() {
	}

	public Connect(Socket clientSocket) {
		client = clientSocket;
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (Exception e1) {
			try {
				client.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.start();
	}

	public void run() {
		try {
			oos.writeObject(new Date());
			oos.flush();
			// close streams and connections
			ois.close();
			oos.close();
			client.close();
		} catch (Exception e) {
		}
	}
}
