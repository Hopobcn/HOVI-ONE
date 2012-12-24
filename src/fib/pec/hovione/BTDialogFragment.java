package fib.pec.hovione;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BTDialogFragment extends DialogFragment {
	private ArrayList<String>  llistaBt;
	private ArrayAdapter<String> llistaBtAdapter;
	private ListView	llistaBtLayout;
	
	public BTDialogFragment() {
		
	}

	public static BTDialogFragment newInstance(int mStackLevel) {
		// TODO Auto-generated method stub
		BTDialogFragment f = new BTDialogFragment();
		//aqui li afegiriem parametres
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle("BT");
		View view = inflater.inflate(R.layout.btlistdialog, container);
		TextView tmp = (TextView) view.findViewById(R.id.tvProva1);
		tmp.setText("Titol");
		
		llistaBt = new ArrayList<String>();
		llistaBt.clear();
		llistaBt.add(0, "Prova");
		llistaBtAdapter = new ArrayAdapter<String> (this.getActivity(), android.R.layout.simple_list_item_1, llistaBt);
		llistaBtLayout = (ListView) view.findViewById(R.id.listView1);
		llistaBtLayout.setAdapter(llistaBtAdapter);
		return view;
	}

}

