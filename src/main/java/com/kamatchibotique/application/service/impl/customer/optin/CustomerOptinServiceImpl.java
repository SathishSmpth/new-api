package com.kamatchibotique.application.service.impl.customer.optin;



import com.kamatchibotique.application.service.services.customer.optin.CustomerOptinService;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.optin.CustomerOptinRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.optin.CustomerOptin;


@Service
public class CustomerOptinServiceImpl extends CommonServiceImpl<Long, CustomerOptin> implements CustomerOptinService {
	
	
	private CustomerOptinRepository customerOptinRepository;
	
	
	@Autowired
	public CustomerOptinServiceImpl(CustomerOptinRepository customerOptinRepository) {
		super(customerOptinRepository);
		this.customerOptinRepository = customerOptinRepository;
	}

	@Override
	public void optinCumtomer(CustomerOptin optin) throws ServiceException {
		Validate.notNull(optin,"CustomerOptin must not be null");
		
		customerOptinRepository.save(optin);
		

	}

	@Override
	public void optoutCumtomer(CustomerOptin optin) throws ServiceException {
		Validate.notNull(optin,"CustomerOptin must not be null");
		
		customerOptinRepository.delete(optin);

	}

	@Override
	public CustomerOptin findByEmailAddress(MerchantStore store, String emailAddress, String code) throws ServiceException {
		return customerOptinRepository.findByMerchantAndCodeAndEmail(store.getId(), code, emailAddress);
	}

}
