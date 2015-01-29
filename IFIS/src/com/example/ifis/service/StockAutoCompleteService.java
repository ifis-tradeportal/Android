package com.example.ifis.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class StockAutoCompleteService {
	public static List<String> getAddressesFromText(String address) {
		final String NAMESPACE = "http://www.ifis.com.sg/";
		final String URL = "http://www.ifis.com.sg/CodeDbWS/Service.asmx";
		final String SOAP_ACTION = "http://www.ifis.com.sg/SearchCounterByName";
		final String METHOD_NAME = "SearchCounterByName";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("SearchKey",address);
		request.addProperty("Exchange","SGX");
		request.addProperty("Limit","20");
		request.addProperty("Login","test");
		request.addProperty("Pwd","test");
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
 				SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		//Set output SOAP object
		
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true; 
		envelope.setAddAdornments(false);
		
	    
	    List<String> simpleAddresses = new ArrayList<String>();
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			//Get the response
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
			for(int i=0;i<rows;i++){
				
				Node fstNode =  nodeLst.item(i);
				//String fn= fstNode.toString();
				NamedNodeMap attributes = fstNode.getAttributes();

				       Node theAttribute = attributes.getNamedItem("RicCode");
				       String sID =theAttribute.getNodeValue();
				       Node theAttribute2 = attributes.getNamedItem("ShortName");
				       String en2=theAttribute2.getNodeValue();
				       simpleAddresses.add(en2+"  ("+sID+")");
				
				
			}
			

	    	
			return simpleAddresses;
	     
	        
	    }
	      
	     catch (Exception e) {
	      return null;
	    }
	  }

}
