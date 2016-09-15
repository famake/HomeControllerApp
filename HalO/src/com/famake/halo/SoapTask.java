package com.famake.halo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class SoapTask extends AsyncTask<Void, Void, Boolean> {

	public static final String HOST = "192.168.1.2:8080";
	//public static final String HOST = "dev.fa2k.net:9090";
	private final String namespace, service, method;
	private final List<Object> parameters;
	private final List<String> parameter_names;
	private final Activity activity;
	private String error;
	private Object soap_result;
	private CompletionCallback callback;
	private boolean showError;
	
	public interface CompletionCallback {
		void success(Object result);
	}
	
	public static abstract class FragmentCompletionCallback 
			implements CompletionCallback {
		
		private final WeakReference<Fragment> frag;
		
		public FragmentCompletionCallback(Fragment frag) {
			this.frag = new WeakReference<Fragment>(frag);
		}
		
		@Override
		public void success(Object result) {
			Fragment fragment = frag.get();
			if (fragment != null && fragment.isVisible())
				successIfVisible(result);
		}
		
		public abstract void successIfVisible(Object result);
		
	}

	public SoapTask(Activity activity, boolean showError,
			String namespace, 
			String service,	String method, 
			List<String> parameter_names,
			List<Object> parameters, 
			CompletionCallback callback) {
		this.activity = activity;
		this.showError = showError;
		this.namespace = namespace;
		this.service = service;
		this.method = method;
		this.parameters = parameters;
		this.parameter_names = parameter_names;
		this.callback = callback;
	}
	
	public SoapTask(Activity activity, boolean showError,
			String namespace, String service, 
			String method, String parameter_name,
			Object parameter, CompletionCallback callback) {
		this(activity, showError, namespace, service, method, 
				new ArrayList<String>(1), 
				new ArrayList<Object>(1), callback);
		this.parameter_names.add(parameter_name);
		this.parameters.add(parameter);
	}
	

	public SoapTask(Activity activity, boolean showError,
			String namespace, String service, 
			String method, CompletionCallback callback) {
		this(activity, showError, namespace, service, method, 
				new ArrayList<String>(), 
				new ArrayList<Object>(), callback);
	}
	
	public void setShowError(boolean show) {
		showError = show;
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0) {
		boolean ok = false;
		SoapObject request = new SoapObject("http://"+namespace+"/", method);
		if (parameters != null && parameter_names != null) {
			int length = parameters.size();
			for (int i=0; i<length; ++i) {
				Object o = parameters.get(i);
				PropertyInfo pi = new PropertyInfo();
				pi.type = o.getClass();
				pi.name = parameter_names.get(i);
				request.addProperty(pi, o);	
			}	
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = 
				new HttpTransportSE("http://" + HOST + "/" + 
							service + "Service/" + service + "?wsdl");
		try {
			androidHttpTransport.call(method, envelope);
			soap_result = envelope.getResponse();
			ok = true;
		} catch (IOException e) {
			error = e.toString();
		} catch (XmlPullParserException e) {
			error = e.toString();
		}
		return ok;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			if (callback != null)
				callback.success(soap_result);
		}
		else if (showError) {
			Utility.showMessage(error, activity);
		}
	}

}
