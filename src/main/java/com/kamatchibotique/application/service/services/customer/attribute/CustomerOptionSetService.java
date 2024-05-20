package com.kamatchibotique.application.service.services.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.attribute.CustomerOption;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionSet;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionValue;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


public interface CustomerOptionSetService extends CommonService<Long, CustomerOptionSet> {



	void saveOrUpdate(CustomerOptionSet entity) throws ServiceException;




	List<CustomerOptionSet> listByStore(MerchantStore store,
			Language language) throws ServiceException;




	List<CustomerOptionSet> listByOption(CustomerOption option,
			MerchantStore store) throws ServiceException;
	

	List<CustomerOptionSet> listByOptionValue(CustomerOptionValue optionValue,
			MerchantStore store) throws ServiceException;

}
