package fib.pec.hovione;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BTDialogFragment extends DialogFragment {
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
		View view = inflater.inflate(R.layout.btlistdialog, container);
		
		return view;		
	}

}

