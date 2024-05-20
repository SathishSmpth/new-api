package com.kamatchibotique.application.service.services.reference.currency;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.reference.currency.Currency;

public interface CurrencyService extends CommonService<Long, Currency> {

	Currency getByCode(String code);

}
