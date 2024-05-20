package com.kamatchibotique.application.repository.customer;

import com.kamatchibotique.application.model.customer.CustomerCriteria;
import com.kamatchibotique.application.model.customer.CustomerList;
import com.kamatchibotique.application.model.merchant.MerchantStore;



public interface CustomerRepositoryCustom {

	CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);
	

}
