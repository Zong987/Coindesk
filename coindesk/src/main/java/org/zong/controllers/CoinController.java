package org.zong.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zong.coindesk.entities.Coin;
import org.zong.coindesk.repositories.CoinRepository;
import org.zong.services.CoinService;

@RestController
public class CoinController {
	
	@Autowired
	private CoinRepository coinRepository;
	
	@Autowired
	private CoinService coinService;
	
	//查詢
	@GetMapping("/search")
	public Object search(@RequestParam String code) {
		Coin coin = this.coinRepository.findByCode(code);
		if(coin != null) {
			return coin;
		} else {
			return new Coin("未找到", "未找到");
		}
		
	}
	
	//新增
	@PostMapping("/insert")
	public Coin insert(@RequestParam String code, @RequestParam String chineseCode) {
		return coinService.insert(code, chineseCode);
	}
	
	//修改
	@PostMapping("/update")
	public Coin update(@RequestParam String code, @RequestParam String chineseCode) {
		if(coinService.update(code, chineseCode) != null) {
			return this.coinRepository.findByCode(code);
		} else {
			return new Coin("未找到", "未找到");
		}
	}
	
	//刪除
	@PostMapping("/delete")
	public void delete(@RequestParam String code) {
		this.coinRepository.deleteByCode(code);
	}
	
	//維護幣別DB
	@Scheduled(cron = "5 * * * * *")
	@GetMapping("/updateByApi")
	public void updateCoin() {
		coinService.updateCoin();
	}
	
	//呼叫coindesk API
	@GetMapping("/coindesk")
	public String coindesk() {
        return coinService.coindesk();
	}
	
	//呼叫coindesk API，組成新資料並回傳
	
	@GetMapping("/newCoindesk")
	public Object newCoindesk() {
		return coinService.newCoindesk().toString();
	}
}
