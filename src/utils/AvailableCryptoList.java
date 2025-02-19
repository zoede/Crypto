package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AvailableCryptoList {
	private static AvailableCryptoList instance = null;
	
	private Map<String, String> tickerIDMap = new HashMap<>();
	
	private List<String> availableCryptosList = new ArrayList<>();
	
	public static AvailableCryptoList getInstance() {
		if (instance == null)
			instance = new AvailableCryptoList();
		
		return instance;
	}
	
	private AvailableCryptoList() {
		findAvailableCryptos();
	}
	
	public void call() {
		String urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=VNEY4VV2AWF1EB51";
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode == 200) {
				String inline = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				sc.close();
				System.out.println(inline);
//				JsonArray jsonArray = new JsonParser().parse(inline).getAsJsonArray();
//				int size = jsonArray.size();
//				
//				String name, id;
//				for (int i = 0; i < size; i++) {
//					JsonObject object = jsonArray.get(i).getAsJsonObject();
//					name = object.get("name").getAsString();
//					id = object.get("id").getAsString();
//					
//					availableCryptosMap.put(name, id);
//					availableCryptosList.add(name);
//				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
	}
	
	private void findAvailableCryptos() {

		String urlString = 
				"https://api.coingecko.com/api/v3/coins/markets" + 
						"?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
//		ALPHAVANTAGE API KEY = VNEY4VV2AWF1EB51
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode == 200) {
				String inline = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				sc.close();
				JsonArray jsonArray = new JsonParser().parse(inline).getAsJsonArray();
				int size = jsonArray.size();
				
				String name, id, symbol;
				for (int i = 0; i < size; i++) {
					JsonObject object = jsonArray.get(i).getAsJsonObject();
					name = object.get("name").getAsString();
					id = object.get("id").getAsString();
					symbol = object.get("symbol").getAsString();
				
					
					tickerIDMap.put(symbol, id);
					
					availableCryptosList.add(symbol);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
	}
	
	public String[] getAvailableCryptos() {
		return availableCryptosList.toArray(new String[availableCryptosList.size()]);
	}
	
	public String coinAvailable(String[] coinList) {
		
		String[] availableCoins = availableCryptosList.toArray(new String[availableCryptosList.size()]);
		
		for (int i=0; i < coinList.length; i++) {
			if (!Arrays.asList(availableCoins).contains(coinList[i].toLowerCase())) {
				return coinList[i];
			}
		} 
		return null;
	}
	
	public String getCryptoIDfromTicker(String tickerName) {
		return tickerIDMap.get(tickerName.toLowerCase());
	}
	
	public static void main(String[] args) {
		
		AvailableCryptoList tester = new AvailableCryptoList();
		tester.findAvailableCryptos();
		
		String[] testArray = tester.getAvailableCryptos();
		
		for (int i=0; i < testArray.length; i++) {
			System.out.println(testArray[i]);
		}
		String[] coins = {"BTC"};
		
//		System.out.println("okay");
//		System.out.println(tester.getCryptoIDfromTicker("BTC"));
		
	}

}
