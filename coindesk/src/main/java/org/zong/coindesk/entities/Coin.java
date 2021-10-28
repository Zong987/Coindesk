package org.zong.coindesk.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coin")
public class Coin {
    
    @Id
    private String code;
    private String chineseCode;
    
    public Coin() {}
	
	public Coin(String code, String chineseCode){
		this.code = code;
		this.chineseCode = chineseCode;
	}
	
    public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getChineseCode() {
		return chineseCode;
	}


	public void setChineseCode(String chineseCode) {
		this.chineseCode = chineseCode;
	}
    
    @Override
    public String toString() {
        return "Coin{" + "code=" + code + ", chineseCode=" + chineseCode +'}';
    }    
}
