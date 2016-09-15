package com.famake.lighting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.famake.halo.R;
import com.famake.halo.Refreshable;
import com.famake.halo.SoapTask;

public class LightingFragment extends Fragment implements Refreshable {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.fragment_lighting, container, false);
			
		((Button)v.findViewById(R.id.lightsOffButton))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SoapTask st = new SoapTask(getActivity(), true,
							"lifx", "LifxBulbControl", "allOff", null);
					st.execute();
				}
			});
		
		((Button)v.findViewById(R.id.on))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoapTask st = new SoapTask(getActivity(), true,
						"lifx", "LifxBulbControl", "allOn", null);
				st.execute();
			}
		});
		
		return v;
	}
	
	@Override
	public void refresh(boolean active) {
	}
	
}
