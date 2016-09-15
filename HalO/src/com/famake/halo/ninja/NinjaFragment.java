package com.famake.halo.ninja;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.famake.halo.MainActivity;
import com.famake.halo.R;
import com.famake.halo.Refreshable;
import com.famake.halo.SoapTask;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NinjaFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link NinjaFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class NinjaFragment extends Fragment implements Refreshable {

	public static int [] switchId = {
		R.id.ninjaSwitch1, R.id.ninjaSwitch2, R.id.ninjaSwitch3
	};
	
	public NinjaFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_ninja, container, false);
		
		refreshButtons(v, false);

		for (int i=0; i<switchId.length; ++i) {
			final int this_i = i;
			final ToggleButton button = 
					((ToggleButton)v.findViewById(switchId[this_i]));

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<Object> values= new ArrayList<Object>();
					names.add("bryterId");
					values.add(this_i);
					names.add("paa");
					values.add(button.isChecked());
					SoapTask buttonClickTask = 
							new SoapTask(getActivity(), true,
							"ninja", "NinjaAPI", "settBryter",
							names, values, null);
					buttonClickTask.execute();
				}
			});
		}

		// Inflate the layout for this fragment
		return v;
	}

	private void refreshButtons(final View fragment, boolean active) {
		SoapTask tempTask = new SoapTask(getActivity(), active,
				"ninja", "NinjaAPI", "getTemperature", 
				new SoapTask.FragmentCompletionCallback(this) {
			
			@Override
			public void successIfVisible(Object result) {
				((TextView)fragment.findViewById(R.id.temperatureValue1)).
						setText(result.toString());
			}
		});
		tempTask.execute();
		for (int i=0; i<switchId.length; ++i) {
			final int this_i = i;
			final ToggleButton button = 
					((ToggleButton)fragment.findViewById(switchId[this_i]));
			
			SoapTask buttonTask = new SoapTask(getActivity(), active,
					"ninja", "NinjaAPI", "getSwitchState",
					"id", this_i,
					new SoapTask.FragmentCompletionCallback(this) {
				
				@Override
				public void successIfVisible(Object result) {
					button.setChecked("true".equals(result.toString()));
				}
			});
			buttonTask.execute();
		}

	}
	
	@Override
	public void refresh(boolean active) {
		final View fragment = getView();
		refreshButtons(fragment, active);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		((MainActivity)getActivity()).removeRefreshable(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh(false);
		((MainActivity)getActivity()).addRefreshable(this);
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
