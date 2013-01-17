package fib.pec.hovione;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class BTManagingConThread extends Thread {
	private final BluetoothSocket socket;
	private final InputStream InStream;
	private final OutputStream OutStream;
	private Handler localH;
	
	public BTManagingConThread(BluetoothSocket mmSocket, Handler m) {
		socket = mmSocket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
			if (tmpIn == null || tmpOut == null) 
				throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InStream = tmpIn;
		OutStream = tmpOut;
		localH = m;
	}
	
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;

		try {
			BTManagingConThread.sleep(1);
		} catch (InterruptedException e1) {
			System.out.println("FUCK YOU");
			e1.printStackTrace();
		}
		while (socket.isConnected() == false) {
			System.out.println("waiting");
		}
		//boolean enviarPeticio = true;
		while (true) {
			try {									

				if (socket.isConnected()) {
					bytes = InStream.read(buffer);
					String cosa = new String(buffer);
					
					Message msg = localH.obtainMessage(1,cosa);
					localH.sendMessage(msg);
					
					System.out.println("Llegit:" + cosa + " numbytes " + bytes);
				} else {
					System.out.println("isConnected() == false -> No llegeix");
				}
				
			} catch (IOException e) {
				System.out.println("FUCK YOU");
				break;
			}			
		}
	}
	
	public void write(byte[] bytes) {
		try {
			if (socket.isConnected()) {
				OutStream.write(bytes);
			} else {
				System.out.println("isConnected() == false -> No escriu");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			
		}
	}
}
