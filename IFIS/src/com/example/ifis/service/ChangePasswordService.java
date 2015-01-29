package com.example.ifis.service;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.ifis.ConstantsStrings;

import android.util.Log;

public class ChangePasswordService {
	public static boolean changePassword(String PUserID,String POrgPwd,String PNewPwd) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS.asmx";
		final String SOAP_ACTION = "http://OMS/ChangeUserPwd";
		final String METHOD_NAME = "ChangeUserPwd";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("PUserID",PUserID);
		request.addProperty("POrgPwd",POrgPwd);
		request.addProperty("PNewPwd",PNewPwd);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
 				SoapEnvelope.VER11);
		envelope.dotNet = true;	
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true; 
		envelope.setAddAdornments(false);	    
		
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();			
			String resStr=response.toString();
			if(resStr.substring(0,1).toUpperCase().equals("S")){
				return true;
			}
			else return false;
	    }
	      
	     catch (Exception e) {
	      return false;
	    }
	  }

}