package com.famake.halo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TvFragment extends Fragment {

	public TvFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tv, container, false);
		
		Button tvOnOff = (Button)v.findViewById(R.id.tvOnOff);
		tvOnOff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleTvPower();
			}

		});
		
		return v;
	}

	private void toggleTvPower() {
		SoapTask st = new SoapTask(getActivity(), true,
						"fjernkontroll", "Arduino", 
						"toggleCableBoxOn", null);
		st.execute();
	}
}
