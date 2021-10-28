package org.zong.coindesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.zong.controllers.CoinController;
import org.zong.services.CoinService;

@SpringBootApplication
@ComponentScan(basePackageClasses = {CoinController.class, CoinService.class})
public class CoindeskApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CoindeskApplication.class, args);
	}

}
