package com.famake.halo.receiver;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SoapRequests {

	public static final String HOST = "192.168.1.2:8080";
	//public static final String HOST = "dev.fa2k.net:9090";
	
    public static void call(String method) throws Exception {
		SoapObject request = new SoapObject("http://fjernkontroll/", method);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = 
				new HttpTransportSE("http://" + HOST + "/ForsterkerService/Forsterker?wsdl");
		androidHttpTransport.call(method, envelope);
    }
    

    public static void callWithParameter(String method, String parName, Object par) throws Exception {
		SoapObject request = new SoapObject("http://fjernkontroll/", method);
		PropertyInfo pi = new PropertyInfo();
		pi.type = Integer.class;
		pi.name = parName;
		request.addProperty(pi, par);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = 
				new HttpTransportSE("http://" + HOST + "/ForsterkerService/Forsterker?wsdl");
		androidHttpTransport.call(method, envelope);
    }

	public static Object callWithResult(String method) throws Exception {
		SoapObject request = new SoapObject("http://fjernkontroll/", method);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = 
				new HttpTransportSE("http://" + HOST + "/ForsterkerService/Forsterker?wsdl");
		androidHttpTransport.call(method, envelope);
		return envelope.getResponse();
    }
	

	public static List<Kilde> hentKildeListe() throws Exception {
		SoapObject request = new SoapObject("http://fjernkontroll/", "kildeListe");
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE httpTransport = 
				new HttpTransportSE("http://" + HOST + "/ForsterkerService/Forsterker?wsdl");
		
		httpTransport.call("kildeListe", envelope);
		SoapObject body = (SoapObject)envelope.bodyIn;
		ArrayList<Kilde> ret = new ArrayList<Kilde>();
		for(int i= 0; i< body.getPropertyCount(); i++){
			SoapObject kso = (SoapObject)body.getProperty(i);
			int index = Integer.parseInt(kso.getProperty("index").toString());
			String navn = (String)kso.getProperty("navn").toString();
	        ret.add(new Kilde(index, navn)); 
	    }
		return ret;
    }
	
	
}
