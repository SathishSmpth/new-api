package com.kamatchibotique.application.service.services.customer.optin;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.optin.CustomerOptin;

public interface CustomerOptinService extends CommonService<Long, CustomerOptin> {

	void optinCumtomer(CustomerOptin optin) throws ServiceException;

	void optoutCumtomer(CustomerOptin optin) throws ServiceException;

	CustomerOptin findByEmailAddress(MerchantStore store, String emailAddress, String code) throws ServiceException;
	

}
