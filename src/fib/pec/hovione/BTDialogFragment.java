package fib.pec.hovione;

import java.io.IOException;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BTDialogFragment extends DialogFragment {
	private ArrayList<String>  llistaBt;
	private ArrayAdapter<String> llistaBtAdapter;
	private ListView	llistaBtLayout;
	private Button discoverButton;
	private BluetoothDevice remoteDevice;
	private BluetoothSocket socket;
	
	
	
	
	public static BTDialogFragment newInstance(int mStackLevel) {
		// TODO Auto-generated method stub
		BTDialogFragment f = new BTDialogFragment();
		//aqui li afegiriem parametres
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.btlistdialog, container);
		TextView tmp = (TextView) view.findViewById(R.id.tvProva1);
		tmp.setText("Titol");
		
		discoverButton = (Button) view.findViewById(R.id.button_discover);
		
		llistaBt = ((MainActivity) this.getActivity()).getFoundDevices();
		if (llistaBt.size() == 0) llistaBt.add("No hi ha cap dispositiu trobat");
		llistaBtAdapter = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, llistaBt);
		llistaBtLayout = (ListView) view.findViewById(R.id.listView1);
		llistaBtLayout.setAdapter(llistaBtAdapter);
		configure_listeners();
		
		return view;
	}
	
	public void notifyListHasToUpdate() {
		llistaBtAdapter.notifyDataSetChanged();
	}
	
	private void configure_listeners() {
		discoverButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			    ((MainActivity)getActivity()).beginDiscovery();
			}
			
		});
		
		llistaBtLayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedItem = (String) llistaBtLayout.getItemAtPosition(arg2);
				String f[] = selectedItem.split("\n");
				String macaddr = null;
				if (f[1] != null) {
					macaddr = f[1];
					BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
					try {
						remoteDevice = btAdapter.getRemoteDevice(macaddr);						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					((MainActivity)getActivity()).crearThreadConnexio(remoteDevice); 
				}
			}
			
		});		
	}
}

