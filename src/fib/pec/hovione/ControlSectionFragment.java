package fib.pec.hovione;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ControlSectionFragment extends Fragment {
	//TODO completar el DummySectionFragment de forma que s'instancii el fragment correcte per a cada Secció.
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private SeekBar motor_fan;
	private SeekBar motor_propeller;
	private Button button_esquerra;
	private Button button_dreta;
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public ControlSectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.home_layout, container, false);
		
		

		motor_fan = (SeekBar) v.findViewById(R.id.motor_fan);
		motor_fan.setEnabled(false);
		motor_fan.setProgress(0);
		motor_fan.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				((MainActivity) getActivity()).enviarStringBT("B");		
				byte[] valor = new byte[1];
				valor[0] = Integer.valueOf(progress).byteValue();
				((MainActivity) getActivity()).enviarByteBT(valor);	
				System.out.format("%x \n", valor[0]);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		motor_propeller = (SeekBar) v.findViewById(R.id.motor_propeller);
		motor_propeller.setEnabled(false);
		motor_propeller.setProgress(0);
		motor_propeller.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				((MainActivity) getActivity()).enviarStringBT("C");		
				byte[] valor = new byte[1];
				valor[0] = Integer.valueOf(progress).byteValue();
				((MainActivity) getActivity()).enviarByteBT(valor);		
				System.out.format("%x \n", valor[0]);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		button_esquerra = (Button) v.findViewById(R.id.button_esquerra);
		button_esquerra.setEnabled(false);
		button_esquerra.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				((MainActivity) getActivity()).enviarStringBT("D");
				((MainActivity) getActivity()).enviarStringBT("M");
			}
		});

		button_dreta = (Button) v.findViewById(R.id.button_dreta);
		button_dreta.setEnabled(false);
		button_dreta.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				((MainActivity) getActivity()).enviarStringBT("D");
				((MainActivity) getActivity()).enviarStringBT("L");
			}
		});
		
		Switch sw = (Switch) v.findViewById(R.id.switch_onOf);
		sw.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					motor_fan.setEnabled(true);
					motor_propeller.setEnabled(true);
					button_esquerra.setEnabled(true);
					button_dreta.setEnabled(true);
					((MainActivity) getActivity()).activaDesactivaBotoWraperMain(true);
				} else {
					motor_fan.setEnabled(false);
					motor_fan.setProgress(0);
					motor_propeller.setEnabled(false);
					motor_propeller.setProgress(0);
					button_esquerra.setEnabled(false);
					button_dreta.setEnabled(false);
					((MainActivity) getActivity()).activaDesactivaBotoWraperMain(false);
				}
			}
			
		});
		
		return v;
	}
}