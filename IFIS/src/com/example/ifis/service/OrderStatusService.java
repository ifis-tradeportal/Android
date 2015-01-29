package com.example.ifis.service;

import java.io.StringReader;
import java.util.ArrayList;

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
import com.example.ifis.model.OrderBook;
import com.example.ifis.model.OrderInfo;
import com.example.ifis.model.AccountModel;

public class OrderStatusService {
	public static int getOrderStatus(String recID,String UserSession) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"oms/ws_rsOMS.asmx";
		final String SOAP_ACTION = "http://OMS/GetOrderStatus";
		final String METHOD_NAME = "GetOrderStatus";
		int res=-1;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("UserSession",UserSession);
		request.addProperty("recID",recID);
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
			String s= "ddd";
			String k="sdfa";
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();			
			String jj=response.toString();
			if((jj.substring(0,2).toUpperCase().equals("E "))||(jj.substring(0, 2).toUpperCase().equals("R "))){
				return -2;
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;
			
			db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(jj));
			doc = (Document) db.parse(inStream); 
			
			NodeList nodeLst = doc.getElementsByTagName("z:row");
			int rows=nodeLst.getLength();
			if (rows==0) return -1;
			else if (rows==1){
				Node fstNode =  nodeLst.item(0);
				NamedNodeMap attributes = fstNode.getAttributes();				
				Node stats = attributes.getNamedItem("ORDER_STATUS");
				String stat =stats.getNodeValue();
				res=Integer.parseInt(stat);
				
			}
			return res;
			

			
	    }
	      
	     catch (Exception e) {
	      return -1;
	    }
	  }



}
