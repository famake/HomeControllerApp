package com.famake.halo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Utility {

	public static void showMessage(String error, final Activity activity) {
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(activity);
		dlgAlert.setMessage(error);
		dlgAlert.setTitle("Error");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(false);
		dlgAlert.create().show();
		dlgAlert.setPositiveButton("Ok",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	activity.finish();
			        }
			    });
	}	
	
}
