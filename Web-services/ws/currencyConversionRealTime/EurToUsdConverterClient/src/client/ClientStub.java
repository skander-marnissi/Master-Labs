/* Author: Skander Marnissi */

package client;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedLong;

import com.currencysystem.webservices.currencyserver.*;
import com.dataaccess.www.webservicesserver.*;

public class ClientStub {

		public static void main(String[] args) throws ServiceException, RemoteException{
			
			CurrencyServerSoap currencySystem = new CurrencyServerLocator().getCurrencyServerSoap();	
			
			//List<String> currencies = Arrays.asList(new String[]{"AED","AFN","ALL","AOA","ARS","AUD","AZN","BBD","BDT","BGN","BHD","BND","BOB","BRL","BSD","BWP","BYN","BZD","CAD","CDF","CHF","CLP","CNY","COP","CRC","CVE","CZK","DKK","DOP","DZD","EGP","ERN","ETB","EUR","FJD","GBP","GHS","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","INR","IQD","ISK","JMD","JOD","JPY","KES","KHR","KMF","KRW","KWD","KZT","LBP","LKR","LYD","MAD","MDL","MKD","MMK","MOP","MRU","MUR","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","SAR","SEK","SGD","SRD","THB","TMT","TND","TRY","TTD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VND","XAF","XCD","XDR","XOF","XPF","YER","ZAR","ZMW"}); 
			
			//Get currencies
			List<String> currencies = Arrays.asList(currencySystem.activeCurrencies("").split(";"));
			for(int i = 0; i < currencies.size(); i++) {
	    		System.out.println(currencies.get(i));
	    	}
			
			//Get exchange rates
			String str = "";
	    	for(int i = 0; i < currencies.size(); i++) {
	    		double exchangerate = (double)currencySystem.convert("", "EUR", currencies.get(i), (double)1, false, "", CurncsrvReturnRate.curncsrvReturnRateNumber, "", "");
	    		str+= exchangerate + ",";
	    		System.out.println(currencies.get(i) + ", "+ exchangerate);
	    	}
	    	System.out.println(str);
		}
}

