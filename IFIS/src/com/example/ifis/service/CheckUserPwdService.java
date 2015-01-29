package com.example.ifis.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.ifis.ConstantsStrings;

public class CheckUserPwdService {
	

	
	
	public static int CheckUserPwd(String strUserID,String strPwd){
		//Create request
		ConstantsStrings cs = new ConstantsStrings();
		String ip = cs.getSelectedIP();
		final String NAMESPACE = "http://OMS/";
		final String URL = ip + "OMS/ws_rsOMS.asmx";
		final String SOAP_ACTION = "http://OMS/CheckUserPwd";
		final String METHOD_NAME = "CheckUserPwd";
		String auth;

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("strUserID", strUserID);
		request.addProperty("strPwd", strPwd);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true;
		envelope.setAddAdornments(false);

		try {
			// Invole web service
			// androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to fahren static variable
			auth = response.toString();
			if (auth.substring(0, 2).equals("R "))
				return 0;
			if (auth.substring(0, 2).equals("E "))
				return -1;
			else
				return 1;

		} catch (Exception e) {
			return -1;
		}
				
	}
	
	

}
