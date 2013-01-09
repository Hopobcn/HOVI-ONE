package fib.pec.hovione;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class BTManagingConThread extends Thread {
	private final BluetoothSocket socket;
	private final InputStream InStream;
	private final OutputStream OutStream;
	
	public BTManagingConThread(BluetoothSocket mmSocket) {
		socket = mmSocket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InStream = tmpIn;
		OutStream = tmpOut;
	}
	
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;
		
		while (true) {
			try {
				bytes = InStream.read(buffer);
				//handler.obtainMessage etc...
			} catch (IOException e) {
				break;
			}
		}
	}
	
	public void write(byte[] bytes) {
		try {
			OutStream.write(bytes);
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
