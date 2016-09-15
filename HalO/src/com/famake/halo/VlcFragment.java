package com.famake.halo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class VlcFragment extends Fragment {

	public VlcFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.fragment_vlc, container, false);
		
		((Button)v.findViewById(R.id.resumeButton))
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true,
						"vlc", "VlcControl", "resume", null);
				st.execute();
			}
		});
	
		((Button)v.findViewById(R.id.pauseButton))
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true, 
						"vlc", "VlcControl", "pause", null);
				st.execute();
			}
		});
		
		return v;
	}

}
