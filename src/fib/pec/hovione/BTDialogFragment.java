package fib.pec.hovione;

import java.util.ArrayList;

import android.os.Bundle;
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

	public static BTDialogFragment newInstance(int mStackLevel) {
		// TODO Auto-generated method stub
		BTDialogFragment f = new BTDialogFragment();
		//aqui li afegiriem parametres
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//getDialog().setTitle("BT");
		/*View view = inflater.inflate(R.layout.btlistdialog, container);
		TextView tmp = (TextView) view.findViewById(R.id.tvProva1);
		tmp.setText("Titol");
		
		llistaBt = new ArrayList<String>();
		llistaBt.clear();
		llistaBt.add(0, "Prova");
		llistaBtAdapter = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, llistaBt);
		llistaBtLayout = (ListView) view.findViewById(R.id.listView1);
		llistaBtLayout.setAdapter(llistaBtAdapter);*/
		
		
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
	}

}

