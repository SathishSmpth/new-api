package com.kamatchibotique.application.service.services.system;

import java.util.List;

import com.kamatchibotique.application.enums.system.MerchantConfigurationType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.MerchantConfig;
import com.kamatchibotique.application.model.system.MerchantConfiguration;

public interface MerchantConfigurationService extends
		CommonService<Long, MerchantConfiguration> {
	
	MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) throws ServiceException;
	
	void saveOrUpdate(MerchantConfiguration entity) throws ServiceException;

	List<MerchantConfiguration> listByStore(MerchantStore store)
			throws ServiceException;

	List<MerchantConfiguration> listByType(MerchantConfigurationType type,
										   MerchantStore store) throws ServiceException;

	MerchantConfig getMerchantConfig(MerchantStore store)
			throws ServiceException;

	void saveMerchantConfig(MerchantConfig config, MerchantStore store)
			throws ServiceException;

}
