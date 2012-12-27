package fib.pec.hovione;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * FragmentPagerAdapter que retorna un fragment corresponent a 
 * una de les sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	public static final int NUM_PAGES = 3;
	
	private Context appContext;

	public SectionsPagerAdapter(FragmentManager fm, Context con) {
		super(fm);
		appContext = con;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		return fragment;
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
			return appContext.getString(R.string.title_section1).toUpperCase();
		case 1:
			return appContext.getString(R.string.title_section2).toUpperCase();
		case 2:
			return appContext.getString(R.string.title_section3).toUpperCase();
		}
		return null;
	}
}
