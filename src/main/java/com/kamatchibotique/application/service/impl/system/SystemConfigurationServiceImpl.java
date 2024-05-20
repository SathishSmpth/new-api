package com.kamatchibotique.application.service.impl.system;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.system.SystemConfiguration;
import com.kamatchibotique.application.repository.system.SystemConfigurationRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.system.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("systemConfigurationService")
public class SystemConfigurationServiceImpl extends
		CommonServiceImpl<Long, SystemConfiguration> implements
		SystemConfigurationService {

	
	private SystemConfigurationRepository systemConfigurationReposotory;
	
	@Autowired
	public SystemConfigurationServiceImpl(
			SystemConfigurationRepository systemConfigurationReposotory) {
			super(systemConfigurationReposotory);
			this.systemConfigurationReposotory = systemConfigurationReposotory;
	}
	
	public SystemConfiguration getByKey(String key) throws ServiceException {
		return systemConfigurationReposotory.findByKey(key);
	}
	



}
