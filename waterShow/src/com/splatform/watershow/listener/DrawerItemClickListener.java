package com.splatform.watershow.listener;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.splatform.watershow.R;

public class DrawerItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		selectItem(position);
	}

	/** Swaps fragments in the main content view */
//	private void selectItem(int position) {
//		// Create a new fragment and specify the planet to show based on
//		// position
//		Fragment fragment = new PlanetFragment();
//		Bundle args = new Bundle();
//		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//		fragment.setArguments(args);
//		// Insert the fragment by replacing any existing fragment
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction()
//				.replace(R.id.content_frame, fragment).commit();
//		// Highlight the selected item, update the title, and close the drawer
//		mDrawer.setItemChecked(position, true);
//		setTitle(mPlanetTitles[position]);
//		mDrawerLayout.closeDrawer(mDrawer);
//
//	}
//
//	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
//	}

}