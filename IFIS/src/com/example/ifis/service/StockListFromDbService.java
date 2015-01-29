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
import com.example.ifis.model.Stock;

public class StockListFromDbService {
public static Stock getStockDetails(String ricCode) {
		
		final String ip="http://118.189.2.46:10901/";
		final String NAMESPACE = "http://OMS/";
		final String URL = ConstantsStrings.IP_109+"OMS/ws_rsOMS.asmx";
		final String SOAP_ACTION = "http://OMS/GetRicInfoFromDatabase";
		final String METHOD_NAME = "GetRicInfoFromDatabase";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("RicCode",ricCode);
		
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
		Stock stkd;
	    
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();	
			String strRes=response.toString();			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;
			db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(strRes));
			doc = (Document) db.parse(inStream);  
			
			NodeList nodeLst = doc.getElementsByTagName("z:row");
			int rows=nodeLst.getLength();
			if(rows==1){
				Node fstNode =  nodeLst.item(0);				
				NamedNodeMap attributes = fstNode.getAttributes();				
				Node stockNameA = attributes.getNamedItem("SHORT_NAME");
				String stockName =stockNameA.getNodeValue();
				//stkd.setStockName(sID);
				Node lastDPA = attributes.getNamedItem("LAST_DONE_PRICE");
				String last =lastDPA.getNodeValue();
				Node bidA = attributes.getNamedItem("BID_PRICE");
				String bid =bidA.getNodeValue();
				Node askA = attributes.getNamedItem("ASK_PRICE");
				String ask =askA.getNodeValue();
				Node lotA = attributes.getNamedItem("LOT_SIZE");
				String lot =lotA.getNodeValue();
				Node exgRateA = attributes.getNamedItem("EXCHANGE_RATE");
				String exgRate =exgRateA.getNodeValue();
				Node currNameA = attributes.getNamedItem("CURRENCY_NAME");
				String currName =currNameA.getNodeValue();
				Node currCodeA = attributes.getNamedItem("CURRENCY_CODE");
				String currCode =currCodeA.getNodeValue();
				Node exgA = attributes.getNamedItem("EXCHANGE");
				String exg =exgA.getNodeValue();
				Node prevClosA = attributes.getNamedItem("PREV_CLOSED_PRICE");
				String prevClosedPrice=prevClosA.getNodeValue();
				stkd= new Stock(stockName, last, bid, ask, lot, exgRate, currName, currCode , exg, prevClosedPrice);
				String kk= stkd.getBidPrice();
				String wa=stkd.getCurrencyName();
				stkd.setStockId(ricCode);
				return stkd;
				
			}
			else return null;
			
 
	        
	    }catch (Exception e) {
	      return null;
	    }
	  }

}
