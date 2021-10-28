package org.zong.coindesk;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.zong.coindesk.entities.Coin;
import org.zong.coindesk.repositories.CoinRepository;
import org.zong.controllers.CoinController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoindeskApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CoinControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private CoinController coinController;
	
	@MockBean
    private CoinRepository coinRepository;
	
	@Test
	public void searchCoinTest() throws Exception {
		mvc.perform(get("/search?code=USD")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void insertCoinTest() throws Exception {
		mvc.perform(post("/insert?code=TTB&chineseCode=踢踢幣")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void updateCoinTest() throws Exception {
		mvc.perform(post("/update?code=USD&chineseCode=美幣")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void deleteCoinTest() throws Exception {
		mvc.perform(post("/delete?code=USD")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void callCoindeskApiTest() throws Exception {
		mvc.perform(get("/coindesk")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void callNewCoindeskApiTest() throws Exception {
		mvc.perform(get("/newCoindesk")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}

}
