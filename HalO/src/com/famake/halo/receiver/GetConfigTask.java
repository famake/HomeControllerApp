package com.famake.halo.receiver;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ToggleButton;

import com.famake.halo.R;
import com.famake.halo.Utility;

public class GetConfigTask extends AsyncTask<Boolean, Void, Boolean> {

	private List<Kilde> kilder;
	private int aktivKilde;
	private boolean muted, på;
	private String error = null;
	private String listeningMode;
	private Activity main;
	private ReceiverFragment receiver;
	
	public GetConfigTask(Activity main, ReceiverFragment receiver) {
		this.main = main;
		this.kilder = null;
		this.receiver = receiver;
	}
	
	@Override
	protected Boolean doInBackground(Boolean... arg0) {
		Boolean hentKilder = arg0[0];
		try {
			på = Boolean.parseBoolean(SoapRequests.callWithResult("erPaa").toString());
			if (hentKilder)
				kilder = SoapRequests.hentKildeListe();
			if (på) {
				aktivKilde = Integer.parseInt(SoapRequests.callWithResult("getKilde").toString());
				muted = Boolean.parseBoolean(SoapRequests.callWithResult("erMuted").toString());
				listeningMode = SoapRequests.callWithResult("getListeningMode").toString(); // Mikael 
			}
			return true;
		} catch (Exception e) {
			error = e.toString();
			return false;
		}
	}

	protected void onPostExecute(Boolean result) {
		if (result) {
			((ToggleButton)main.findViewById(R.id.forsterkerButton)).setChecked(på);
			((ToggleButton)main.findViewById(R.id.muteButton)).setChecked(muted);
			((Button)main.findViewById(R.id.listenModeButton)).setText(listeningMode);
			if (kilder != null)
				receiver.setKilder(kilder);
			receiver.setAktivKilde(aktivKilde);
		}
		else {
			Utility.showMessage(error, main);
		}
	}
	
}
