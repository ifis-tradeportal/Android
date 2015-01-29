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

import com.example.ifis.ConstantsStrings;
import com.example.ifis.model.AccountModel;

public class GetAccountsService  {
	public static ArrayList<AccountModel> getAccounts(String UserID,String UserSession) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS.asmx?op=GetTradeAccount";
		//final String URL = ConstantsStrings.IP_109+"OMS/ws_rsOMS.asmx?op=GetTradeAccount";
		final String SOAP_ACTION = "http://OMS/GetTradeAccount";
		final String METHOD_NAME = "GetTradeAccount";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("UserSession",UserSession);
		request.addProperty("UserID",UserID);
		
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
		
	    
		ArrayList<AccountModel> CustomListViewValuesArr = new ArrayList<AccountModel>();
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
			
			AccountModel fake= new AccountModel();
			
			CustomListViewValuesArr.add(fake);
			NodeList nodeLst = doc.getElementsByTagName("z:row");
			int rows=nodeLst.getLength();
			for(int i=0;i<rows;i++){
				
				AccountModel sp= new AccountModel();
				Node fstNode =  nodeLst.item(i);
				NamedNodeMap attributes = fstNode.getAttributes();
				Node theAttribute = attributes.getNamedItem("TRADE_ACC_ID");
				String sID =theAttribute.getNodeValue();
				sp.setAccountId(sID);
				
				Node theAttribute2 = attributes.getNamedItem("TRADE_ACC_NAME");
				String en2=theAttribute2.getNodeValue();
				sp.setAccountName(en2);
				CustomListViewValuesArr.add(sp);
				
				
			}
			

	    	
			return CustomListViewValuesArr;
	     
	        
	    }
	      
	     catch (Exception e) {
	      return null;
	    }
	  }
	


	
}
