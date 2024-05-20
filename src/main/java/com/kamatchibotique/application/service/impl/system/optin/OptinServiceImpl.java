package com.kamatchibotique.application.service.impl.system.optin;

import com.kamatchibotique.application.enums.system.OptinType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.optin.Optin;
import com.kamatchibotique.application.repository.system.OptinRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.system.optin.OptinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptinServiceImpl extends CommonServiceImpl<Long, Optin> implements OptinService {
	
	
	private OptinRepository optinRepository;
	
	@Autowired
	public OptinServiceImpl(OptinRepository optinRepository) {
		super(optinRepository);
		this.optinRepository = optinRepository;
	}


	@Override
	public Optin getOptinByCode(MerchantStore store, String code) throws ServiceException {
		return optinRepository.findByMerchantAndCode(store.getId(), code);
	}

	@Override
	public Optin getOptinByMerchantAndType(MerchantStore store, OptinType type) throws ServiceException {
		return optinRepository.findByMerchantAndType(store.getId(), type);
	}

}
