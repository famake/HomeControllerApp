package com.famake.halo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class VentilationFragment extends Fragment implements Refreshable {

	private int[] BOXID = {
			R.id.tempBox1
	};
	private TextView[] tempBox = new TextView[BOXID.length];
	private TextView fanStateText;
	private RadioButton fanModeOn, fanModeOff, fanModeAuto;
	private boolean fanOn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_ventilation, container, false);
		
		for (int i=0; i<BOXID.length; ++i) {
			TextView tv = (TextView) v.findViewById(BOXID[i]);
			tempBox[i] = tv;
		}
		fanStateText = (TextView)v.findViewById(R.id.fanStateText);
		
		Button setButton = (Button)v.findViewById(R.id.setButton);
		setButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fanModeAuto.isChecked()) {
					SoapTask task = new SoapTask(getActivity(),
						true, "hus", "VentilationController", 
						"setFanAuto",
								new SoapTask.FragmentCompletionCallback(VentilationFragment.this) {
									public void successIfVisible(Object result) {
										fanModeAuto.setChecked(true);
									}
								}
							);
					task.execute();
				}
				else {
					final boolean onPar = fanModeOn.isChecked();
					SoapTask task = new SoapTask(getActivity(),
							true, "hus", "VentilationController", 
							"setFanManual", "fanState", onPar,
									new SoapTask.FragmentCompletionCallback(VentilationFragment.this) {
										public void successIfVisible(Object result) {
											fanModeOn.setChecked(onPar);
											fanModeOff.setChecked(!onPar);
										}
									}
								);
						task.execute();
				}
			}
		});
		
		fanModeOn = (RadioButton)v.findViewById(R.id.fanModeOn);
		fanModeOff = (RadioButton)v.findViewById(R.id.fanModeOff);
		fanModeAuto = (RadioButton)v.findViewById(R.id.fanModeAuto);
		
		
		refresh(false);
		return v;
	}
	
	
	@Override
	public void refresh(boolean active) {
		for (int i=0; i<BOXID.length; ++i) {
			final TextView tv = tempBox[i];
			SoapTask task = new SoapTask(getActivity(),
					active,
					"hus", "VentilationController", 
					"getSensorTemperature", "sensor", 
					Integer.valueOf(i),
						new SoapTask.FragmentCompletionCallback(this) {
							public void successIfVisible(Object result) {
								tv.setText(result.toString());
							}
						}
					);
			task.execute();
		}
		SoapTask updateButtons = new SoapTask(getActivity(),
				active, "hus", "VentilationController", 
				"isFanAuto",
					new SoapTask.FragmentCompletionCallback(this) {
						public void successIfVisible(Object result) {
							if ("true".equals(result.toString())) {
								fanModeAuto.setChecked(true);
								fanModeOn.setChecked(false);
								fanModeOff.setChecked(false);
							}
							else {
								fanModeAuto.setChecked(false);
								if (fanOn) {
									fanModeOn.setChecked(true);
								}
								else {
									fanModeOff.setChecked(true);
								}
							}
						}
					}
				);
		updateButtons.execute();
		SoapTask updateFanState = new SoapTask(getActivity(),
				active,
				"hus", "VentilationController", 
				"getFanState",
					new SoapTask.FragmentCompletionCallback(this) {
						public void successIfVisible(Object result) {
							fanOn = "true".equals(result.toString());
							if (!fanModeAuto.isChecked()) {
								fanModeOn.setChecked(fanOn);
								fanModeOff.setChecked(!fanOn);
							}
							if (fanOn) {
								fanStateText.setText("On");
							}
							else {
								fanStateText.setText("Off");
							}
						}
					}
				);
		updateFanState.execute();
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
	
	
}
