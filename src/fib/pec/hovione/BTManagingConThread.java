package fib.pec.hovione;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		byte[] tam = new byte[4];
		int tamany = -1;
		int numE = 0;
		int num = 0;
		String auxiliar = "";
		try {
			BTManagingConThread.sleep(1);
		} catch (InterruptedException e1) {
			System.out.println("FUCK YOU");
			e1.printStackTrace();
		}
		while (socket.isConnected() == false) {
			//System.out.println("waiting");
		}
		
		/*if (socket.isConnected()) {
			try {
				
			for (int i = 0 ; i < 12000;++i) 
			{
				br = InStream.read();
				if(br != -1)
				auxiliar = auxiliar + (char) br;
				//System.out.print((char) br);
			}
			System.out.println(auxiliar);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		//boolean enviarPeticio = true;
		String missatge = "";
	    while (true) {
			try {									

				if (socket.isConnected()) {
					for (int i = 0; i < 4; ++i) {
						tamany += InStream.read(tam,i,1);
						//System.out.format("%x \n",tam[i]);
						//System.out.println("es tamany " + tamany);
					}
					//if(tamany == 4) {
						String aux = new String(tam);
						num = Integer.parseInt(aux);
						numE = ((num/250)+1)*30;
						num = num*2 + numE;
						buffer = new byte[num];
						System.out.println("num + numE " + num + "numE" +numE);

					//}
					for(int i = 0; i < num; ++i) {
						//tamany = InStream.read(buffer, i, 1);
						System.out.println("la i del segon for" + i );
						tamany = InStream.read(buffer, 0, 1);
						//if(tamany != -1) {
							missatge = missatge + new String(buffer,0,1);

						//}
					}
					
					System.out.println(" Missatge " + missatge);
					System.out.println("es tamany " + tamany);
					//int t = Integer.getInteger(tam.toString());
					//int t = Integer.parseInt(tam.toString());
					/*
					buffer = new byte[1024];
					bytes = InStream.read(buffer);*/
					String str = missatge.replace(" ", "");
					System.out.print(str);
					
					byte[] arrayOfValues = new byte[str.length()/2];
					for (int i = 0; i < arrayOfValues.length; ++i){
						arrayOfValues[i] = (byte) Integer.parseInt(str.substring(2*i, 2*i+2), 16);
					}
					
					Bitmap bm = BitmapFactory.decodeByteArray(arrayOfValues, 0,arrayOfValues.length);
					//byte array a buffered image
					
					Message msg = localH.obtainMessage(1,bm);
					localH.sendMessage(msg);
					
					//System.out.println(" tamany " + t);
				} else {
					System.out.println("isConnected() == false -> No llegeix");
				}
				
			} catch (IOException e) {
				System.out.println("FUCK YOU");
				//break;
			}			
		}
	}
	
	public void write(byte[] bytes) {
		try {
			if (socket.isConnected()) {
				OutStream.write(bytes);

				System.out.println("Enviats bytes " + bytes);
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
