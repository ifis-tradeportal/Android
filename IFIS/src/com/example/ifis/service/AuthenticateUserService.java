package com.example.ifis.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.ifis.ConstantsStrings;
import com.example.ifis.model.OrderBook;

public class AuthenticateUserService {
	
	
	
	public static HashMap<Integer,String>AuthenticateUser(String strUserID,String strPwd,String strUserSession){

		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		
		 final  String NAMESPACE = "http://OMS/";
		 final  String URL = ip+"/OMS/ws_rsOMS2.asmx";
		 //final  String URL = ConstantsStrings.IP_109+"/OMS/ws_rsOMS2.asmx";
		 final  String SOAP_ACTION = "http://OMS/AuthenticateUser";
		 final  String METHOD_NAME = "AuthenticateUser";
		 String auth;
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("strUserID", strUserID);
		request.addProperty("strPwd", strPwd);
		request.addProperty("strUserSession", strUserSession);
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
		Map<Integer, String> authMap = new HashMap<>();

		try {
			// Invole web service
			// androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to fahren static variable
			auth = response.toString();
			if (auth.substring(0, 2).equals("R ")){
				authMap.put(0, "");
				return (HashMap<Integer, String>) authMap;
			}
				
			if (auth.substring(0, 2).equals("E ")){
				authMap.put(-1, "");
				return (HashMap<Integer, String>) authMap;
				
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;
			
			db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(auth));
			doc = (Document) db.parse(inStream); 
			
			NodeList nodeLst = doc.getElementsByTagName("z:row");
			int rows=nodeLst.getLength();
			for(int i=0;i<rows;i++){
				Node fstNode =  nodeLst.item(i);
				NamedNodeMap attributes = fstNode.getAttributes();				
				Node stockAt = attributes.getNamedItem("TR_CODE");
				String trCode =stockAt.getNodeValue();
				authMap.put(1, trCode);
			}
			return (HashMap<Integer, String>) authMap;

		} catch (Exception e) {
			authMap.put(-1, "");
			return (HashMap<Integer, String>) authMap;
		}
				
	}
	
	

}
