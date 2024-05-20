package com.kamatchibotique.application.service.impl.system;

import com.kamatchibotique.application.model.system.MerchantLog;
import com.kamatchibotique.application.repository.system.MerchantLogRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.system.MerchantLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("merchantLogService")
public class MerchantLogServiceImpl extends
		CommonServiceImpl<Long, MerchantLog> implements
		MerchantLogService {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantLogServiceImpl.class);


	
	private MerchantLogRepository merchantLogRepository;
	
	@Autowired
	public MerchantLogServiceImpl(
			MerchantLogRepository merchantLogRepository) {
			super(merchantLogRepository);
			this.merchantLogRepository = merchantLogRepository;
	}


	




}
