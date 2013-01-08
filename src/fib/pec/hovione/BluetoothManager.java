package fib.pec.hovione;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

public class BluetoothManager {

	public static final int REQUEST_ENABLE_BT = 1; //constant per identificar peticio de engegar bluetooth
	
	private BluetoothAdapter localBluetooth;
	
	private FragmentActivity act;
	
	
	public BluetoothManager(FragmentActivity activ) {
	    localBluetooth = BluetoothAdapter.getDefaultAdapter();
	    act = activ;
	}
	
	public boolean isBluetoothSupported() {
		return localBluetooth != null;
	}
	
	public boolean isBluetoothEnabled() {
		return localBluetooth.isEnabled();
	}
	
	public void enableBluetooth() {
		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		act.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);		
	}
	
	public Set<BluetoothDevice> getPairedDevices() {
		return localBluetooth.getBondedDevices();
	}
	
	public void performDiscovery() {
		// Registrar el BroadcastReciver
		if (localBluetooth.isDiscovering()) localBluetooth.cancelDiscovery();
		//localBluetooth.startDiscovery();
		BroadcastReceiver mReceiver = ((MainActivity) act).getDiscoveryReceiver();
		
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		((MainActivity) act).registerReceiver(mReceiver, filter); //des-registrar al onDestroy
		
		// Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        ((MainActivity) act).registerReceiver(mReceiver, filter);
        
        localBluetooth.startDiscovery();
	}
        
   public void endDiscovery() {
	   localBluetooth.cancelDiscovery();
   }
	
}
