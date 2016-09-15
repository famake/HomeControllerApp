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
public class SonosFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.fragment_sonos, container, false);
		
		((Button)v.findViewById(R.id.sonosPlay))
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true,
						"sonos", "SonosBean", "play", null);
				st.execute();
			}
		});
	
		
		return v;
	}

}
