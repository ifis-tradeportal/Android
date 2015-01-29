package com.example.ifis.service;

import java.io.StringReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.example.ifis.model.OrderInfo;
import com.example.ifis.model.AccountModel;

public class NewOrderService {
	public static boolean makeNewOrder(OrderInfo order,String UserSession) {
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		//String ip ="http://118.189.2.46:7010/";
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS2.asmx";
		final String SOAP_ACTION = "http://OMS/NewOrderFixIncome";
		final String METHOD_NAME = "NewOrderFixIncome";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date today = Calendar.getInstance().getTime();
		String data="AlgoStartTime=" +
				"~Exchange=" +order.getExchange()+
				"~FI_PriceCode=7" +
				"~ExchangeRate=" +order.getExgRate()+
				"~AlPercent=" +
				"~OrderSize=" +order.getQuantity()+
				"~VoiceLog=" +
				"~UpdateBy=" +order.getUpdateBy()+
				"~SecCode=" +order.getSymbol()+
				"~ClientAccID=" +order.getTradeAccID()+
				"~SpecialInstruction=" +
				"~FI_NumberAgent=5" +
				"~ExtraCare=0" +
				"~AlgoWouldQty=" +
				"~FI_TaxPercent=2" +
				"~BuySell=" +order.getSide()+
				"~Yield=0" +
				"~ForceOrderStatus=" +1+
				"~AlgoEndTime=" +
				"~SecurityType=STOCK" +
				"~OrderType=" +order.getOrderType()+
				"~FI_TaxType=CHAR" +
				"~AltSymbol=" +
				"~StockLocation=" +
				"~TimeInForce=0" +
				"~NumberOfDaysAccuredInterest=1" +
				"~OrderPrice=" +order.getPrice()+
				"~SettCurr=" +order.getSettCurr()+
				"~FI_TotalNetCashAmount=4" +
				"~FI_TaxAmount=3" +
				"~FI_TotalAccuredInterst=8" +
				"~TradeCurrency=" +order.getSettCurr()+
				"~tradeOfficer=" +order.getUpdateBy()+
				"~AlgoWouldPrice=" +
				"~FI_PriceType=6" +
				"~ExpireDate="+df.format(today).toString() +
				"~FI_TradeAmount=9" +
				"~AlAuction=" +
				"~AlgoStrategy=0" +
				"~AlRelLimit=" +
				"~AlBenchMark=~";
		
		
		request.addProperty("UserSession",UserSession);
		request.addProperty("strData",data);
		request.addProperty("nVersion","0");	
		
		
		
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
	    	//androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			String s= "ddd";
			String k="sdfa";
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();			
			String jj=response.toString();	
			if(jj.equals("S")) return true;
			else return false;    
	    }
	      
	     catch (Exception e) {
	      return false;
	    }
	  }
	
	
	public static String[] GetClientAccountTradeLimit(String UserSession,String accID) {
		ConstantsStrings cs= new ConstantsStrings();
		String ip =cs.getSelectedIP();
		final String NAMESPACE = "http://OMS/";
		final String URL = ip+"OMS/ws_rsOMS2.asmx";
		final String SOAP_ACTION = "http://OMS/GetClientAccountTradeLimit";
		final String METHOD_NAME = "GetClientAccountTradeLimit";
SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("UserSession",UserSession);
		request.addProperty("Key",accID);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
 				SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true; 
		envelope.setAddAdornments(false);	    
		String limts[];
	    try {
	    	androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(SOAP_ACTION, envelope);
			String s= "ddd";
			String k="sdfa";
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();			
			String jj=response.toString();
			if((jj.substring(0,2).toUpperCase().equals("E "))||(jj.substring(0, 2).toUpperCase().equals("R "))){
				limts = new String[] {"E"};
				return limts;
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
			if (rows==0); //return -1;
			else if (rows==1){
				Node fstNode =  nodeLst.item(0);
				NamedNodeMap attributes = fstNode.getAttributes();				
				Node stats = attributes.getNamedItem("BUY_LIMIT");
				String stat1 =stats.getNodeValue();
								
				Node stats2 = attributes.getNamedItem("BUY_USED_LIMIT");
				String stat2 =stats.getNodeValue();
				float buyLt= Float.valueOf(stat1)-Float.valueOf(stat2);
				
				Node stats3 = attributes.getNamedItem("BUY_LIMIT");
				String stat3 =stats.getNodeValue();
								
				Node stats4 = attributes.getNamedItem("BUY_USED_LIMIT");
				String stat4 =stats.getNodeValue();
				float sellLt= Float.valueOf(stat3)-Float.valueOf(stat4);
				
				limts = new String[] {"OK",String.valueOf(buyLt),String.valueOf(sellLt)};
				
				return limts;
				
				
			}
			return null ;
			

			
	    }
	      
	     catch (Exception e) {
	      return null;
	    }
		
		
	}

}
