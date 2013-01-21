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

public class PhotoSectionFragment extends Fragment {
	//TODO completar el DummySectionFragment de forma que s'instancii el fragment correcte per a cada Secció.
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public PhotoSectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.photo_layout, container, false);
		
		Button ferFoto = (Button) v.findViewById(R.id.demanarFoto);
		ferFoto.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				((MainActivity) getActivity()).enviarStringBT("A");
			}
		});
		
		return v;
	}
}