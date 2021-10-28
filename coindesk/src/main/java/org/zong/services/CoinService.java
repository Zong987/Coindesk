package org.zong.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zong.coindesk.entities.Coin;
import org.zong.coindesk.repositories.CoinRepository;

@Service
public class CoinService {
	static Map<String, String> codeArr = Stream.of(new String[][] {
		{"USD", "美金"},
		{"GBP", "英鎊"},
		{"EUR", "歐元"}
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	@Autowired
	private CoinRepository coinRepository;
	
	//查詢
	public Object search(@RequestParam String code) {
		updateCoin();
		return this.coinRepository.findByCode(code);
	}
	
	//新增
	public Coin insert(@RequestParam String code, @RequestParam String chineseCode) {
		Coin c = new Coin();
		c.setCode(code);
		c.setChineseCode(chineseCode);
		this.coinRepository.save(c);
		return this.coinRepository.findByCode(code);
	}
	
	//修改
	public Coin update(String code, String chineseCode) {
		updateCoin();
		Coin c = new Coin();
		c.setCode(code);
		c.setChineseCode(chineseCode);
		this.coinRepository.save(c);
		return this.coinRepository.findByCode(code);
	}
	
	//刪除
	@PostMapping("/delete")
	public void delete(String code) {
		updateCoin();
		this.coinRepository.deleteByCode(code);
	}
	
	//維護幣別DB
	public void updateCoin() {
		JSONObject coindesk = new JSONObject(coindesk());
		JSONObject time = coindesk.getJSONObject("time");
		String updateTime = time.getString("updatedISO");
		System.out.println(updateTime);
		
		JSONObject bpi = coindesk.getJSONObject("bpi");
		for(String code : codeArr.keySet()) {
			Coin c = getCoin(bpi.getJSONObject(code));
			coinRepository.save(c);
		}
	}
	
	//呼叫coindesk API
	public String coindesk() {
		String requestUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
		StringBuffer buffer = new StringBuffer();
        try {  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.connect();  
            
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return buffer.toString();  
	}
	
	//呼叫coindesk API，組成新資料並回傳
	
	public Object newCoindesk() {
		updateCoin();
		JSONObject newdesk = new JSONObject();
		
		JSONObject coindesk = new JSONObject(coindesk());
		JSONObject time = coindesk.getJSONObject("time");
		String updateTime = time.getString("updatedISO");
		ZonedDateTime dateTime = ZonedDateTime.parse(updateTime);
		String res = dateTime.withZoneSameInstant(ZoneId.of("UTC+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
		newdesk.put("更新時間", res);
		
		JSONObject bpi = coindesk.getJSONObject("bpi");
		JSONArray newCoinArray = new JSONArray();
		for(String code : codeArr.keySet()) {
			JSONObject coin = bpi.getJSONObject(code);
			JSONObject newCoin = new JSONObject();
			newCoin.put("幣別", coin.get("code"));
			if(this.coinRepository.findByCode(code) != null) {
				newCoin.put("幣別中文名稱", this.coinRepository.findByCode(code).getChineseCode());
			} else {
				newCoin.put("幣別中文名稱", "XX幣");
			}
			
			newCoin.put("匯率", coin.get("rate"));
			newCoinArray.put(newCoin);
		}
		newdesk.put("幣別相關資訊", newCoinArray);
		
		return newdesk.toString();
	}
	
	//解析coindeskAPI資料
	public static Coin getCoin(JSONObject j) {
		Coin c = new Coin();
		c.setCode(j.getString("code"));
		c.setChineseCode(codeArr.get(c.getCode()));
		return c;
	}
}
