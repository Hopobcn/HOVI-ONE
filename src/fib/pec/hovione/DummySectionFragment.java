package fib.pec.hovione;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DummySectionFragment extends Fragment {
	//TODO completar el DummySectionFragment de forma que s'instancii el fragment correcte per a cada Secció.
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public DummySectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.home, container, false);
		try {
			TextView textView = (TextView) v.findViewById(R.id.terminal);
			Bundle args = new Bundle();
			args = this.getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);
			textView.setText(Integer.valueOf(position));
		} catch(Exception e) {
			System.out.println("PETA TextView");
		}
		
		
		
		Button a = (Button) v.findViewById(R.id.a);
		a.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				
				((MainActivity) getActivity()).enviarStringBT("A");
			}
		});
		
		Button b = (Button) v.findViewById(R.id.b);
		b.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				
				((MainActivity) getActivity()).enviarStringBT("C");
			}
		});

		SeekBar motor1 = (SeekBar) v.findViewById(R.id.motor1);
		motor1.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				((MainActivity) getActivity()).enviarStringBT(Integer.valueOf(progress).toString());		
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				((MainActivity) getActivity()).enviarStringBT("B");		
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		SeekBar motor2 = (SeekBar) v.findViewById(R.id.motor2);
		motor2.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				((MainActivity) getActivity()).enviarStringBT(Integer.valueOf(progress).toString());		
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				((MainActivity) getActivity()).enviarStringBT("C");		
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		return v;
	}
}