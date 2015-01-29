package com.example.ifis.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.example.ifis.model.CustodyStock;


public class CustodyStockService {
	
	public static HashMap<String,ArrayList<CustodyStock>> getCustodyStocks(String PUserSession,String PClientAccount,String PRicCode) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS.asmx?op=GetCustodyStockLocationDetails";
		final String SOAP_ACTION = "http://OMS/GetCustodyStockLocationDetails";
		final String METHOD_NAME = "GetCustodyStockLocationDetails";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("PUserSession",PUserSession);
		request.addProperty("PClientAccount",PClientAccount);
		request.addProperty("PRicCode",PRicCode);
		
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
		
	    Map<String,ArrayList<CustodyStock>> stockMap= new HashMap<String,ArrayList<CustodyStock>>();
		ArrayList<CustodyStock> stockList = new ArrayList<CustodyStock>();
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			String jj=response.toString();
			
			if(jj.substring(0, 2).toUpperCase().equals("E ")){
				stockMap.put("E", null);
				return (HashMap<String, ArrayList<CustodyStock>>) stockMap;
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
			if (rows==0) {
				stockMap.put("OK", stockList);
				return (HashMap<String, ArrayList<CustodyStock>>) stockMap;
			}
			for(int i=0;i<rows;i++){
				
				CustodyStock stk= new CustodyStock();
				
				Node fstNode =  nodeLst.item(i);
				NamedNodeMap attributes = fstNode.getAttributes();	
				
				Node stockAt = attributes.getNamedItem("RICCODE");
				String stock =stockAt.getNodeValue();
				stk.setStockCode(stock);
				
				Node odrQtyA = attributes.getNamedItem("TOTAL");
				String ordQty =odrQtyA.getNodeValue();
				stk.setTotal(ordQty);
				
				Node snameA = attributes.getNamedItem("STOCK_NAME");
				String snameStr =snameA.getNodeValue();
				stk.setStockName(snameStr);
				
				Node locA = attributes.getNamedItem("STOCK_LOCATION");
				String locStr =locA.getNodeValue();
				stk.setLocation(locStr);
				
				
				stockList.add(stk);
				stockMap.put("OK", stockList);
				
			}
			

	    	
			return (HashMap<String, ArrayList<CustodyStock>>) stockMap;
	     
	        
	    }
	      
	     catch (Exception e) {
	      return null;
	    }
	  }


}
