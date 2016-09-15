package com.famake.halo.receiver;

import android.os.AsyncTask;
import android.widget.ToggleButton;

import com.famake.halo.MainActivity;
import com.famake.halo.R;
import com.famake.halo.Utility;

public class SetVolumeTask extends AsyncTask<Integer, Void, Integer> {

	public static final int VOLUME_UP = 0;
	public static final int VOLUME_DOWN = 1;
	public static final int MUTE = 2;
	public static final int UNMUTE = 3;

	private final MainActivity main;
	private String error;
	private int vol;
	
	public SetVolumeTask(MainActivity main) {
		this.main = main; 
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			if (params.length == 1) {
				int code = params[0];
				switch(code) {
				case VOLUME_UP:
					vol = Integer.parseInt(SoapRequests.callWithResult("volumOpp").toString());
					break;
				case VOLUME_DOWN:
					vol = Integer.parseInt(SoapRequests.callWithResult("volumNed").toString());
					break;
				case MUTE:
					SoapRequests.call("mute");
					break;
				case UNMUTE:
					SoapRequests.call("unmute");
					vol = Integer.parseInt(SoapRequests.callWithResult("getVolum").toString());
					break;
				}
				return code;
			}
			error = "Bad programmer";
			return -1;
		} catch(Exception e) {
			error = e.toString();
			return -1;
		}
	}
	
	protected void onPostExecute(Integer result) {
		switch (result) {
		case MUTE:
			((ToggleButton)main.findViewById(R.id.muteButton)).setChecked(true);
			break;
		case UNMUTE:
			((ToggleButton)main.findViewById(R.id.muteButton)).setChecked(false);
		case VOLUME_DOWN:
		case VOLUME_UP:
			main.setVolume(vol);
			break;
		case -1:
			Utility.showMessage(error, main);
			break;
		}
	}
}
