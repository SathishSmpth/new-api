package com.kamatchibotique.application.service.services.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionValue;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


public interface CustomerOptionValueService extends CommonService<Long, CustomerOptionValue> {



	List<CustomerOptionValue> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	void saveOrUpdate(CustomerOptionValue entity) throws ServiceException;

	CustomerOptionValue getByCode(MerchantStore store, String optionValueCode);



}
