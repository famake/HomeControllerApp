package com.famake.halo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ProjectorFragment extends Fragment
			implements SoapTask.CompletionCallback, Refreshable {

	private ToggleButton tbOverride;
	private ToggleButton power;
	private ToggleButton pictureMode;
	
	private Activity activity;
	
	public ProjectorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_projector, container, false);
		tbOverride = (ToggleButton)v.findViewById(R.id.screenUpButton);
		tbOverride.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setScreenOverride(tbOverride.isChecked());
				System.out.println("Clicked on " + ProjectorFragment.this);
			}
		});

		power = (ToggleButton)v.findViewById(R.id.projectorPower);
		
		power.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPowerState(power.isChecked());
			}
		});

		pictureMode = (ToggleButton)v.findViewById(R.id.pictureMode);
		pictureMode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPictureMode(pictureMode.isChecked());
			}
		});

		Button sleepTimer30 = (Button)v.findViewById(R.id.sleepTimer30);
		Button sleepTimer60 = (Button)v.findViewById(R.id.sleepTimer60);
		Button sleepTimer90 = (Button)v.findViewById(R.id.sleepTimer90);
		Button sleepTimer120 = (Button)v.findViewById(R.id.sleepTimer120);
		Button sleepTimerOff = (Button)v.findViewById(R.id.sleepTimerOff);
		
		sleepTimer30.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sleepTimer(30);
			}
		});
		
		sleepTimer60.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sleepTimer(60);
			}
		});
		
		sleepTimer90.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sleepTimer(90);
			}
		});
		
		sleepTimer120.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sleepTimer(120);
			}
		});
		
		sleepTimerOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sleepTimerOff();
			}
		});
		
		return v;
	}
	
	
	public void refresh(boolean active) {
		System.out.println("Refreshing " + this);
		SoapTask st = new SoapTask(getActivity(), active, 
				"fjernkontroll", "Projector", "getPower", 
				new SoapTask.FragmentCompletionCallback(this) {
					@Override
					public void successIfVisible(Object result) {
						power.setChecked("true".equals(result.toString()));
					}
				});
		st.execute();
		st = new SoapTask(getActivity(), active,
				"fjernkontroll", "Arduino", "getScreenUpOverride", 
				this);
		st.execute();
	}
	
	

	protected void setPowerState(boolean checked) {
		SoapTask st = new SoapTask(getActivity(), true, 
				"fjernkontroll", "Projector", "setPower",
				"on", checked, null);
		st.execute();
	}
	protected void setPictureMode(boolean mode) {
		SoapTask st;
		if(mode) {
			st = new SoapTask(getActivity(), true, 
				"fjernkontroll", "Projector", "SetPictureMode",
				"mode", "day", null);
		}
		else{
			st = new SoapTask(getActivity(), true, 
					"fjernkontroll", "Projector", "SetPictureMode",
					"mode", "night", null);
		}
		st.execute();
	}
	
	protected void sleepTimer(int time) {
		SoapTask st = new SoapTask(getActivity(), true, 
				"fjernkontroll", "Projector", "sleepTimer",
				"time", time, null);
		st.execute();
	}
	
	private void sleepTimerOff() {
		SoapTask st = new SoapTask(getActivity(), true, 
				"fjernkontroll", "Projector", "sleepTimerOff", null);
		st.execute();
		
	}
	

	private void setScreenOverride(boolean checked) {
		SoapTask st = new SoapTask(getActivity(), true, 
				"fjernkontroll", "Arduino", "setScreenUpOverride",
				"on", checked, null);
		st.execute();
	}

	@Override
	public void success(Object result) {
		if (isVisible())
			tbOverride.setChecked("true".equals(result.toString()));
	}
	

	@Override
	public void onPause() {
		super.onPause();
		((MainActivity)getActivity()).removeRefreshable(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh(false);
		((MainActivity)getActivity()).addRefreshable(this);
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //TODO mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

	
}
