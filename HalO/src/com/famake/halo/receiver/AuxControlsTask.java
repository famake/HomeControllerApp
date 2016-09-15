package com.famake.halo.receiver;

import com.famake.halo.R;
import com.famake.halo.Utility;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class AuxControlsTask extends AsyncTask<Integer, Void, Integer> {

	public static final int NEXT_LISTENING_MODE = 0, AV = 1, PÅ = 2;
	private String error;
	private Activity main;
	String response = "test";
	
	public AuxControlsTask(Activity main) {
		this.main = main;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			if (params.length == 1) {
				int code = params[0];
				if (code == NEXT_LISTENING_MODE) {
					//SoapRequests.call("nesteLytteModus");
					response = SoapRequests.callWithResult("nesteLytteModus").toString();
					//((TextView)main.findViewById(R.id.volumeText)).setText(response);
				}
				else if (code == AV) {
					SoapRequests.call("av");
				}
				else if (code == PÅ) {
					SoapRequests.call("paa");
				}
				return code;
			}
			return -1;
		} catch(Exception e) {
			error = e.toString();
			return -1;
		}
	}

	protected void onPostExecute(Integer code) {
		if (code == -1) {
			Utility.showMessage(error, main);
		}
		else if(code == 0)
		{
			((TextView)main.findViewById(R.id.listenModeButton)).setText(response);
		}
	}

}
