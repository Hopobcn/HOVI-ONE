package fib.pec.hovione;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class BTClientThread extends Thread {
	private BluetoothSocket socket;
    private BluetoothDevice remoteDevice;
    private BluetoothAdapter btAdapter;
    private Handler localH;
    
    //private static final UUID MY_UUID_SECURE =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        
	public BTClientThread(BluetoothDevice dev, Handler h) {
		remoteDevice = dev;
		BluetoothSocket tmp = null;
		localH = h;
		
		Method m;
		try {			
			//tmp = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
			m = remoteDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
			tmp = (BluetoothSocket) m.invoke(remoteDevice, 1);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket = tmp;
	}
	
	public void run() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btAdapter.cancelDiscovery();
		
		try {
			socket.connect();
			
			Message msg = localH.obtainMessage(0,"SUCCESFUL_CON");
			localH.sendMessage(msg);
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
			Message msg2 = localH.obtainMessage(2,"UNSUCCESFUL_CON");
			localH.sendMessage(msg2);
		}		
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
