package com.kamatchibotique.application.service.services.system;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.system.SystemConfiguration;

public interface SystemConfigurationService extends
		CommonService<Long, SystemConfiguration> {
	
	SystemConfiguration getByKey(String key) throws ServiceException;

}
