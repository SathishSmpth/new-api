package com.kamatchibotique.application.service.impl.reference.currency;

import com.kamatchibotique.application.model.reference.currency.Currency;
import com.kamatchibotique.application.repository.reference.currency.CurrencyRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.reference.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("currencyService")
public class CurrencyServiceImpl extends CommonServiceImpl<Long, Currency>
	implements CurrencyService {
	
	private CurrencyRepository currencyRepository;
	
	@Autowired
	public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
		super(currencyRepository);
		this.currencyRepository = currencyRepository;
	}

	@Override
	public Currency getByCode(String code) {
		return currencyRepository.getByCode(code);
	}

}
