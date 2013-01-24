package fib.pec.hovione;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PhotoSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	private Bitmap imageBitmap = null;
	private ImageView iv = null;
	
	public PhotoSectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.photo_layout, container, false);
		
		Button ferFoto = (Button) v.findViewById(R.id.demanarFoto);
		ferFoto.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
				((MainActivity) getActivity()).enviarStringBT("A");
				System.out.println("hem apretat A");
			}
		});
		
		if (imageBitmap != null) {
			iv = (ImageView) v.findViewById(R.id.imageView);
			iv.setImageBitmap(imageBitmap);
		}
		
		return v;
	}
	
	public void setImageBitmap(Bitmap auxBitmap) {		
		imageBitmap = auxBitmap;
	}
}