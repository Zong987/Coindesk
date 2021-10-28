package org.zong.coindesk.repositories;

import org.zong.coindesk.entities.Coin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends CrudRepository<Coin, Long> {
	public Coin findByCode(String code);
	
	public long deleteByCode(String code);
}
