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
import com.example.ifis.model.OrderBook;
import com.example.ifis.model.AccountModel;

public class OrderBookService {
	public static  HashMap<String, ArrayList<OrderBook>> getOrderBook(String UserID,String UserSession) {
		
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS.asmx?op=GetOrderByUserID";
		final String SOAP_ACTION = "http://OMS/GetOrderByUserID";
		final String METHOD_NAME = "GetOrderByUserID";
		
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
		Map<String, ArrayList<OrderBook>> orderMap = new HashMap<String, ArrayList<OrderBook>>();
		
		ArrayList<OrderBook> orderBookList = new ArrayList<OrderBook>();
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			String jj=response.toString();
			if(jj.substring(0, 3).toUpperCase().equals("E  ")){
				orderMap.put("E", null);
				return (HashMap<String, ArrayList<OrderBook>>) orderMap;
			}
			
			else if(jj.substring(0, 2).toUpperCase().equals("E ")){
				orderMap.put("OK", orderBookList);
				return (HashMap<String, ArrayList<OrderBook>>) orderMap;
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
			for(int i=0;i<rows;i++){
				
				OrderBook ob= new OrderBook();
				Node fstNode =  nodeLst.item(i);
				NamedNodeMap attributes = fstNode.getAttributes();				
				Node stockAt = attributes.getNamedItem("Stock");
				String stock =stockAt.getNodeValue();
				ob.setStock(stock);
				Node BSatt = attributes.getNamedItem("c4");
				String buySell =BSatt.getNodeValue();
				ob.setBS(buySell);
				Node odrQtyA = attributes.getNamedItem("OrderQty");
				String ordQty =odrQtyA.getNodeValue();
				ob.setOrderQty(ordQty);
				Node opA = attributes.getNamedItem("OrderPrice");
				String oPrice =opA.getNodeValue();
				ob.setOrderPrice(oPrice);
				Node osA = attributes.getNamedItem("c7");
				String oStatus =osA.getNodeValue();
				ob.setOrderStatus(oStatus);
				Node xcgA = attributes.getNamedItem("Exchange");
				String xng =xcgA.getNodeValue();
				ob.setExchange(xng);
				Node accA = attributes.getNamedItem("c9");
				String acc =accA.getNodeValue();
				ob.setClientAC(acc);
				Node datea = attributes.getNamedItem("c11");
				String date =datea.getNodeValue();
				ob.setOrderDateTime(date);
				Node fqA = attributes.getNamedItem("FilledQty");
				String fulqty =fqA.getNodeValue();
				ob.setMatchQty(fulqty);
				Node apa = attributes.getNamedItem("AvePrice");
				String avgP =apa.getNodeValue();
				ob.setAvePrice(avgP);
				Node seta = attributes.getNamedItem("c22");
				String setC =seta.getNodeValue();
				ob.setSettlementCurr(setC);
				Node sna = attributes.getNamedItem("c43");
				if(sna!=null){
					String stkName =sna.getNodeValue();
					ob.setStockName(stkName);
				}
				else
				ob.setStockName("");
				Node refa = attributes.getNamedItem("c8");
				String ref =refa.getNodeValue();
				ob.setRefNo(ref);
				orderBookList.add(ob);
				orderMap.put("OK", orderBookList);

			}
	
			return (HashMap<String, ArrayList<OrderBook>>) orderMap;
        
	    }
	      
	     catch (Exception e) {
	      return null;
	    }
	  }

	
}
