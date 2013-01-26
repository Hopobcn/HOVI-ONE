package fib.pec.hovione;

import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

/**
 * FragmentPagerAdapter que retorna un fragment corresponent a 
 * una de les sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
	
	public static final int NUM_PAGES = 2;
	
	private PhotoSectionFragment fragmentFoto = null;
	private Context appContext;
	private Bitmap imatgeB;
	
	public SectionsPagerAdapter(FragmentManager fm, Context con) {
		super(fm);
		appContext = con;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		if (position == 0) {
			//PhotoSectionFragment fragment = new PhotoSectionFragment();
			if(fragmentFoto == null) {
				fragmentFoto = new PhotoSectionFragment();
				Bundle args = new Bundle();
				args.putInt(PhotoSectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragmentFoto.setArguments(args);
			}
			fragmentFoto.setImageBitmap(imatgeB);
			
			return fragmentFoto;
		} else /*if (position == 1)*/ {
			Fragment fragment = new ControlSectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		} /*else  {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}*/
		
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return NUM_PAGES;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return appContext.getString(R.string.title_section1).toUpperCase(Locale.getDefault());
		case 1:
			return appContext.getString(R.string.title_section2).toUpperCase(Locale.getDefault());
		/*case 2:
			return appContext.getString(R.string.title_section3).toUpperCase();*/
		}
		return null;
	}
	
	public void destroyAndCreatePhoto(Bitmap bitmap) {
		imatgeB = bitmap;
	}
	
	public void activaDesactivaBotoWraper(boolean on) {
		PhotoSectionFragment fragm = (PhotoSectionFragment) getItem(0);
		fragm.activaDesactivaBoto(on);
	}
}
