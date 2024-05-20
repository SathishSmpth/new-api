package com.kamatchibotique.application.repository.reference.currency;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.reference.currency.Currency;

public interface CurrencyRepository extends JpaRepository <Currency, Long> {

	
	Currency getByCode(String code);
}
