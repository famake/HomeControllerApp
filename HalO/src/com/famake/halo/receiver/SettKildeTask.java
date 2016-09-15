package com.famake.halo.receiver;

import com.famake.halo.R;
import com.famake.halo.Utility;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ToggleButton;

public class SettKildeTask extends AsyncTask<Integer, Void, Integer> {

	private String error ;
	private Activity main;
	private ReceiverFragment receiver;
	
	public SettKildeTask(Activity main, ReceiverFragment receiver) {
		this.main = main;
		this.receiver = receiver;
	}
	
	@Override
	protected Integer doInBackground(Integer... arg0) {
		try {
			SoapRequests.callWithParameter("setKildePaa", "kilde", arg0[0]);
			return arg0[0];
		} catch (Exception e) {
			error = e.toString();
			return -1;
		}
	}
	
	protected void onPostExecute(Integer result) {
		if (result == -1) {
			Utility.showMessage(error, main);
		}
		else {
			((ToggleButton)main.findViewById(R.id.forsterkerButton)).setChecked(true);
			receiver.setAktivKilde(result);
		}
	}

}
