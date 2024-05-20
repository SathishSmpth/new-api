package com.kamatchibotique.application.service.services.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.attribute.CustomerOption;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


public interface CustomerOptionService extends CommonService<Long, CustomerOption> {

	List<CustomerOption> listByStore(MerchantStore store, Language language)
			throws ServiceException;



	void saveOrUpdate(CustomerOption entity) throws ServiceException;



	CustomerOption getByCode(MerchantStore store, String optionCode);




}
