package com.famake.lighting;


import java.util.Arrays;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.famake.halo.SoapTask;

public class SeekSoap implements OnSeekBarChangeListener {
	
	private final Activity activity;
	private final String dmxFunction, parameter;
	private static long lastSend;
	
	public SeekSoap(Activity activity, String dmxFunction, String parameter) {
		this.activity = activity;
		this.dmxFunction = dmxFunction;
		this.parameter = parameter;
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (System.currentTimeMillis() - lastSend > 50) {
			SoapTask st = new SoapTask(activity, true,
					"dmx", "Dmx", dmxFunction,
					Arrays.asList(new String[] {parameter}),
					Arrays.asList(new Object[] {progress}), null);
			st.execute();
			lastSend = System.currentTimeMillis();
		}
	}

}
