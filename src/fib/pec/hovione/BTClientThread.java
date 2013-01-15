package fib.pec.hovione;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;

public class BTClientThread extends Thread {
	private BluetoothSocket socket;
    private BluetoothDevice remoteDevice;
    private BluetoothAdapter btAdapter;
    private Handler localH;
    
    private static final UUID MY_UUID_SECURE =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static final UUID MY_UUID_SECURE =  UUID.fromString("12280fd3-f405-49f8-b26b-5a0b239627d4");
    
	public BTClientThread(BluetoothDevice dev, Handler h) {
		remoteDevice = dev;
		BluetoothSocket tmp = null;
		localH = h;
		
		ParcelUuid[] proba = remoteDevice.getUuids();
		try {
			//socket = remoteDevice.createRfcommSocketToServiceRecord(proba[0].getUuid()); 
			tmp = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = tmp;
	}
	
	public void run() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btAdapter.cancelDiscovery();
		
		try {
			socket.connect();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
		Message msg = localH.obtainMessage(0,"APSEOFIAWEF");
		localH.sendMessage(msg);
	}
	
	public void cancel() {
		try {
            socket.close();
        } catch (IOException e) { }
	}
	
	public BluetoothSocket returnSocket() {
		return socket;
	}
}
