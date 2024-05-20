package com.kamatchibotique.application.service.services.system.optin;

import com.kamatchibotique.application.enums.system.OptinType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.optin.Optin;

public interface OptinService extends CommonService<Long, Optin> {
	
	
	Optin getOptinByMerchantAndType(MerchantStore store, OptinType type) throws ServiceException;
	Optin getOptinByCode(MerchantStore store, String code) throws ServiceException;

}
