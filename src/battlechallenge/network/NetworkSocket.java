package battlechallenge.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkSocket {
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private BufferedInputStream bis;
	
	public boolean isOpen() {
		return socket != null;
	}

	public NetworkSocket(String ip, int port) {
		// TODO: validate port and IP
		try {
			// open a socket connection
			socket = new Socket(ip, port);
			// open I/O streams for objects
			oos = new ObjectOutputStream(socket.getOutputStream());
			bis = new BufferedInputStream(socket.getInputStream());
			ois = new ObjectInputStream(bis);
		} catch (IOException e) {
			// TODO: Handle client cannot connect to server
			System.err.println("Application Exception: Invalid network connection parameters.");
			socket = null;
		}
	}

	public NetworkSocket(Socket socket) {
		try {
			this.socket = socket;
			if (socket != null) {
				// open I/O streams for objects
				oos = new ObjectOutputStream(socket.getOutputStream());
				bis = new BufferedInputStream(socket.getInputStream());
				ois = new ObjectInputStream(bis);
			}
		} catch (IOException e) {
			System.err.println("Application Exception: Invalid network connection parameters.");
			socket = null;
		}
	}

	public void kill() {
		if (socket == null) // Socket already closed
			return;
		try {
			oos.close();
		} catch (IOException e) { /* ignore exceptions */
		}
		try {
			ois.close();
		} catch (IOException e) { /* ignore exceptions */
		}try {
			bis.close();
		} catch (IOException e) { /* ignore exceptions */
		}
		try {
			socket.close();
			socket = null;
		} catch (IOException e) { /* ignore exceptions */
		}
	}
	
	public boolean writeObject(Object o) throws ConnectionLostException {
		if (socket == null)
			throw new ConnectionLostException();
		if (o != null) {
			try {
				// clear object cache
				oos.reset();
				oos.writeObject(o);
				// Ensure objects are sent.
				oos.flush();
				return true;
			} catch (IOException e) {
				System.err.println("Socket Exception: Cannot write to socket.");
				socket = null;
				throw new ConnectionLostException("Socket Exception: Cannot write to socket.");
			}
		}
		return false;
	}
	
	public boolean writeInt(int i) throws ConnectionLostException {
		if (socket == null)
			throw new ConnectionLostException();
		try {
			oos.writeInt(i);
			// Ensure objects are sent.
			oos.flush();
			return true;
		} catch (IOException e) {
			System.err.println("Socket Exception: Cannot write to socket.");
			socket = null;
			throw new ConnectionLostException("Socket Exception: Cannot write to socket.");
		}
	}
	
	public Object readObject(boolean blocking) throws ConnectionLostException {
		if (socket == null)
			throw new ConnectionLostException();
		try {
			/*
			 * check to see if something is arrived in time. Otherwise, assume
			 * no input. 
			 */
			return bis.available() > 0 || blocking ? ois.readObject() : null;
		} catch (IOException e) {
			System.err.println("Socket Exception: Cannot read from socket.");
			socket = null;
			throw new ConnectionLostException("Socket Exception: Cannot write to socket.");
		} catch (ClassNotFoundException e) {
			System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
		}
		return null;
	}
	
	public Object readObject(int timeout) throws ConnectionLostException {
		if (socket == null)
			throw new ConnectionLostException();
		try {
			/*
			 * check to see if something is arrived in time. Otherwise, assume
			 * no input. 
			 */
			if (bis.available() > 0)
				return ois.readObject();
			Thread.sleep(timeout);
			return bis.available() > 0 ? ois.readObject() : null;
		} catch (IOException e) {
			System.err.println("Socket Exception: Cannot read from socket.");
			socket = null;
			throw new ConnectionLostException("Socket Exception: Cannot write to socket.");
		} catch (ClassNotFoundException e) {
			System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	
	public Integer readInt(boolean blocking) throws ConnectionLostException {
		if (socket == null)
			throw new ConnectionLostException();
		try {
			/*
			 * check to see if something is arrived in time. Otherwise, assume
			 * no input. 
			 */
			return bis.available() > 0 || blocking ? ois.readInt(): null;
		} catch (IOException e) {
			System.err.println("Socket Exception: Cannot read from socket.");
			socket = null;
			throw new ConnectionLostException("Socket Exception: Cannot write to socket.");
		}
	}
}
