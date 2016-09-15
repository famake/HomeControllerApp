package com.famake.lighting;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

import com.famake.halo.R;
import com.famake.halo.SoapTask;

public class DmxFragment extends Fragment {

	private boolean pressed;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.fragment_dmx, container, false);
			
		((Button)v.findViewById(R.id.lasers))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "laserOnSound", null);
					st.execute();
				}
			});

		((Button)v.findViewById(R.id.blackout))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true,
						"dmx", "Dmx", "blackout", null);
				st.execute();
			}
		});
		
		((Button)v.findViewById(R.id.strobeFlash))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true,
						"dmx", "Dmx", "strobeFlash", null);
				st.execute();
			}
		});
		
		
		final SeekBar seek = ((SeekBar)v.findViewById(R.id.strobeSpeed));
		((Button)v.findViewById(R.id.strobeHold))
		.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pressed = true;
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "strobeHoldOn", 
							Arrays.asList(new String[] {"speed"}),
							Arrays.asList(new Object[] {
									seek.getProgress()
							}),
							null);
					st.execute();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					pressed = false;
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "strobeHoldRelease", null);
					st.execute();
				}
				return false;
			}
		}		
		);

		((Button)v.findViewById(R.id.smoke))
			.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pressed = true;
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "smokeOn", null);
					st.execute();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					pressed = false;
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "smokeOff", null);
					st.execute();
				}
				return false;
			}
		}		
		);
		
		
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (pressed) {
					SoapTask st = new SoapTask(getActivity(), true,
							"dmx", "Dmx", "strobeHoldOn", 
							Arrays.asList(new String[] {"speed"}),
							Arrays.asList(new Object[] {
									seek.getProgress()
							}),
							null);
					st.execute();
				}
			}
		});
		
		SeekBar red = ((SeekBar)v.findViewById(R.id.red));
		red.setOnSeekBarChangeListener(new SeekSoap(getActivity(), "setColorRailRed", "r"));

		final Switch dart = 
				((Switch)v.findViewById(R.id.dart));
		dart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						SoapTask st = new SoapTask(getActivity(), true,
								"dmx", "Dmx", "setDart", "dartOn", isChecked, null);
						st.execute();
					}
				});
		
		((Button)v.findViewById(R.id.beerPong))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dart.setChecked(false);
				SoapTask st = new SoapTask(getActivity(), true,
						"dmx", "Dmx", "beerPong", null);
				st.execute();
			}
		});
		
		return v;
	}
	
}
