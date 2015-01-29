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

public class AmendOrderService {
	public static int amendOrder(String recID,String UserSession,String uID,String qty,String price,String ordType) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS2.asmx";
		final String SOAP_ACTION = "http://OMS/AmendOrderFixIncome";
		final String METHOD_NAME = "AmendOrderFixIncome";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		Date today = Calendar.getInstance().getTime();	
		String data="OrderSize=" +qty+
				"~OrderPrice=" +price+
				"~ExpireDate=" +df2.format(today).toString()+
				"~OrderType=2" +
				"~TimeInForce=0"+
				"~LastUpdateTime=" +df.format(today).toString()+
				"~RecID=" +recID+
				"~UpdateBy=" +uID+
				"~VoiceLog=~";
		
		
		request.addProperty("UserSession",UserSession);
		request.addProperty("strData",data);
		request.addProperty("nVersion","0");
		
//		request.addProperty("UserSession",UserSession);
//		request.addProperty("RecID",recID);
//		request.addProperty("Qty",qty);		
//		request.addProperty("Price",price);
//		request.addProperty("UpdateBy",uID);
//		request.addProperty("LastUpdateTime",df.format(today).toString());		
//		request.addProperty("OrderType","LIM");
//		request.addProperty("VoiceLog","");			
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
 				SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		//Set output SOAP object
		
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true;
		envelope.setAddAdornments(false);	    
		
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();	
			String jj=response.toString();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;			
			db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(jj));
			doc = (Document) db.parse(inStream);			
			NodeList nodeLst = doc.getElementsByTagName("z:row");
			int rows=nodeLst.getLength();
			if (rows<=0) return -1;
			else if((jj.subSequence(0, 2).equals("E "))||(jj.subSequence(0, 2).equals("R "))){
				return -2;
			}
			else if(rows==1){
				return 1;
			}
			else return -1;
	        
	    }
	      
	     catch (Exception e) {
	      return 0;
	    }
	  }



}
