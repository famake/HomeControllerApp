package com.famake.halo.receiver;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.famake.halo.MainActivity;
import com.famake.halo.R;
import com.famake.halo.Refreshable;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ReceiverFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ReceiverFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class ReceiverFragment extends Fragment implements Refreshable {

	public List<Kilde> kilder;
	public List<ToggleButton> knapper;

	public ReceiverFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_receiver, container, false);
		GetConfigTask cfg = new GetConfigTask(getActivity(), this);
		cfg.execute(true);

		((Button)v.findViewById(R.id.muteButton)).
			setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	mute((ToggleButton)v);
	        }
	    });
		((Button)v.findViewById(R.id.listenModeButton)).
			setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	nextListeningMode();
		        }
		    });
		((Button)v.findViewById(R.id.forsterkerButton)).
			setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	avpå();
		        }
		    });
		// Inflate the layout for this fragment
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void setKilder(List<Kilde> kilder) {
		ArrayList<ToggleButton> knapper = new ArrayList<ToggleButton>();
		LinearLayout ll = ((LinearLayout)getActivity().findViewById(R.id.kildeLayout));
		ll.removeAllViews();
		for (final Kilde k : kilder) {
			ToggleButton tb = new ToggleButton(getActivity());
			tb.setText(k.navn);
			tb.setTextOn(k.navn);
			tb.setTextOff(k.navn);
			final ReceiverFragment fragment = this;
			tb.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	final SettKildeTask sk = new SettKildeTask(getActivity(), fragment);
		        	sk.execute(k.index);
		        }
		    });
			knapper.add(tb);
			ll.addView(tb);
		}
		this.knapper = knapper;
		this.kilder = kilder;
	}
	
	public void setAktivKilde(int kildeId) {
		for (int i=0; i<kilder.size(); ++i) {
			Kilde k = kilder.get(i);
			ToggleButton b = knapper.get(i);
			b.setChecked(kildeId == k.index);
		}
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
	
	

	public void refresh(boolean active) {
		GetConfigTask cfg = new GetConfigTask(getActivity(), this);
		cfg.execute(true);
	}

	private void nextListeningMode() {
		AuxControlsTask act = new AuxControlsTask(getActivity());
		act.execute(AuxControlsTask.NEXT_LISTENING_MODE);
	}
	
	private void avpå() {
		AuxControlsTask act = new AuxControlsTask(getActivity());
		int code;
		if (((ToggleButton)getActivity().findViewById(R.id.forsterkerButton)).isChecked()) {
			code = AuxControlsTask.PÅ;
		}
		else {
			code = AuxControlsTask.AV;
		}
		act.execute(code);
	}

	private void mute(ToggleButton v) {
		int code = v.isChecked() ? SetVolumeTask.MUTE : SetVolumeTask.UNMUTE;
		SetVolumeTask setVol = new SetVolumeTask((MainActivity)getActivity());
		setVol.execute(code);
	}

}
