package com.famake.halo;


import java.lang.ref.WeakReference;
import java.util.HashSet;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.famake.halo.ninja.NinjaFragment;
import com.famake.halo.receiver.ReceiverFragment;
import com.famake.halo.receiver.SetVolumeTask;
import com.famake.lighting.DmxFragment;
import com.famake.lighting.LightingFragment;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private int volume;
	private final HashSet<Refreshable> refreshables;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public MainActivity() {
		refreshables = new HashSet<Refreshable>();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		updateVolume(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		setListener(menu);
		return true;
	}

	private void setListener(Menu menu) {
		MenuItem refreshMenuItem = menu.getItem(1);
		refreshMenuItem.setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					refresh();
					return true;
				}
		});
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.volumeMenuItem).setTitle("Volume: " + volume);
		return true;
	}
	
	
	
	void refresh() {
		updateVolume(true);
		for (Refreshable ref : refreshables) {
			ref.refresh(true);
		}
	}

	private void updateVolume(boolean show_error) {
		SoapTask st = new SoapTask(this, show_error,
				"fjernkontroll", 
				"Forsterker", "getVolum", 
				new SoapTask.CompletionCallback() {
			@Override
			public void success(Object result) {
				volume = Integer.parseInt(result.toString());
				invalidateOptionsMenu();
			}
		});
		st.setShowError(show_error);
		st.execute();
	}
	
	

	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    int action = event.getAction();
	    int keyCode = event.getKeyCode();
        switch (keyCode) {
        case KeyEvent.KEYCODE_VOLUME_UP:
            if (action == KeyEvent.ACTION_UP) {
        		SetVolumeTask setVol = new SetVolumeTask(this);
            	setVol.execute(SetVolumeTask.VOLUME_UP);
            }
            return true;
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            if (action == KeyEvent.ACTION_DOWN) {
        		SetVolumeTask setVol = new SetVolumeTask(this);
            	setVol.execute(SetVolumeTask.VOLUME_DOWN);
            }
            return true;
        default:
            return super.dispatchKeyEvent(event);
        }
    }



	public void setVolume(int vol) {
		this.volume = vol;
		invalidateOptionsMenu();
	}
		
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public static class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		private SparseArray<WeakReference<Fragment>> fragments;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new SparseArray<WeakReference<Fragment>>();
		}
		
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			WeakReference<Fragment> fragref = fragments.get(position);
			Fragment fragment = null;
			if (fragref != null) {
				fragment = fragref.get();
			}
			if (fragment == null) {
				if (position == 0) {
					ReceiverFragment f = new ReceiverFragment();
					fragment = f;
				}
				else if (position == 1) {
					ProjectorFragment f = new ProjectorFragment();
					fragment = f;
				}
				else if (position == 2) {
					TvFragment f = new TvFragment();
					fragment = f;
				}
				else if (position == 3) {
					VlcFragment f = new VlcFragment();
					fragment = f;
				}
				else if (position == 4) {
					NinjaFragment f = new NinjaFragment();
					fragment = f;
				}
				else if (position == 5) {
					VentilationFragment f = new VentilationFragment();
					fragment = f;
				}
				else if (position == 6) {
					LightingFragment f = new LightingFragment();
					fragment = f;
				}
				else if (position == 7) {
					DmxFragment f = new DmxFragment();
					fragment = f;
				}
				else if (position == 8) {
					SonosFragment f = new SonosFragment();
					fragment = f;
				}
				else {
					fragment = new DummySectionFragment();
					Bundle args = new Bundle();
					args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
				}
				fragments.put(position, new WeakReference<Fragment>(fragment));
			}
			return fragment;
		}
		

		@Override
		public int getCount() {
			return 9;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Receiver";
			case 1:
				return "Projector";
			case 2:
				return "TV";
			case 3:
				return "VLC";
			case 4:
				return "Ninja";
			case 5:
				return "Ventilation";
			case 6:
				return "Lighting";
			case 7:
				return "DMX";
			case 8:
				return "Sonos";
			}
			return null;
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public synchronized void addRefreshable(Refreshable refreshable) {
		refreshables.add(refreshable);
	}

	public synchronized void removeRefreshable(Refreshable refreshable) {
		refreshables.remove(refreshable);
	}
	

}
