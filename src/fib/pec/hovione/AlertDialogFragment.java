package fib.pec.hovione;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment { //s'usa DialogFragment com a fragment contenidor dels dialegs
    
	public static AlertDialogFragment newInstance(int title, String message) { //els fragments de dialog els construim aixi ja que si volem passar parametres
		AlertDialogFragment adf = new AlertDialogFragment();   //ho hem de fer fora de constructores ja que android si vol recrear el fragment
		Bundle args = new Bundle();                            //ho fa amb el constructor per defecte que no reb paràmetres i els perdriem.
		args.putInt("title", title);
		args.putString("message", message);
		adf.setArguments(args); //guardem arguments per a posteriors reconstruccions si fa falta.
		return adf;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = this.getArguments().getInt("title");
		String message = this.getArguments().getString("message");
		
		AlertDialog.Builder adb = new AlertDialog.Builder(this.getActivity());
		adb = adb.setTitle(title).setMessage(message);
		
		if (title == R.string.title_bluetooth_enable_petition) {
			adb = adb.setPositiveButton(R.string.button_yes,
					                    new DialogInterface.OnClickListener() {
				
					                        @Override
											public void onClick(DialogInterface dialog, int which) {
					                        	((MainActivity)getActivity()).pressed_Ok_EnableBluetooth();												
											}
										});
			adb = adb.setNegativeButton(R.string.button_no, 
					                    new DialogInterface.OnClickListener() {
				
											@Override
											public void onClick(DialogInterface dialog, int which) {
												((MainActivity)getActivity()).close_application();												
											}
										});
		} else if (title == R.string.title_warning) {
			adb = adb.setPositiveButton(R.string.button_ok,
						                new DialogInterface.OnClickListener() {										
											@Override
											public void onClick(DialogInterface dialog, int which) {
												((MainActivity)getActivity()).close_application();
											}
									 	});
		} else if (title == R.string.title_successful_con) {
			adb = adb.setPositiveButton(R.string.button_ok,
						                new DialogInterface.OnClickListener() {										
											@Override
											public void onClick(DialogInterface dialog, int which) {							
												((MainActivity)getActivity()).notShowBTDialogFragment();							
											}
									    });
		} else if (title == R.string.title_unsuccessful_con) {
			adb = adb.setPositiveButton(R.string.button_ok,
						                new DialogInterface.OnClickListener() {										
											@Override
											public void onClick(DialogInterface dialog, int which) {							
												((MainActivity)getActivity()).close_application();							
											}
									    });
		}
			    
	    return adb.create();
	    			   
	}
	
	
}
