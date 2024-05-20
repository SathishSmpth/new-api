package com.kamatchibotique.application.service.impl.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.customer.attribute.CustomerOptionSetService;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.attribute.CustomerOptionSetRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.customer.attribute.CustomerOption;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionSet;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionValue;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;



@Service("customerOptionSetService")
public class CustomerOptionSetServiceImpl extends
		CommonServiceImpl<Long, CustomerOptionSet> implements CustomerOptionSetService {


	@Autowired
	private CustomerOptionSetRepository customerOptionSetRepository;
	

	@Autowired
	public CustomerOptionSetServiceImpl(
			CustomerOptionSetRepository customerOptionSetRepository) {
			super(customerOptionSetRepository);
			this.customerOptionSetRepository = customerOptionSetRepository;
	}
	

	@Override
	public List<CustomerOptionSet> listByOption(CustomerOption option, MerchantStore store) throws ServiceException {
		Validate.notNull(store,"merchant store cannot be null");
		Validate.notNull(option,"option cannot be null");
		
		return customerOptionSetRepository.findByOptionId(store.getId(), option.getId());
	}
	
	@Override
	public void delete(CustomerOptionSet customerOptionSet) throws ServiceException {
		customerOptionSet = customerOptionSetRepository.findOne(customerOptionSet.getId());
		super.delete(customerOptionSet);
	}
	
	@Override
	public List<CustomerOptionSet> listByStore(MerchantStore store, Language language) throws ServiceException {
		Validate.notNull(store,"merchant store cannot be null");

		
		return customerOptionSetRepository.findByStore(store.getId(),language.getId());
	}


	@Override
	public void saveOrUpdate(CustomerOptionSet entity) throws ServiceException {
		Validate.notNull(entity,"customer option set cannot be null");

		if(entity.getId()>0) {
			super.update(entity);
		} else {
			super.create(entity);
		}
		
	}


	@Override
	public List<CustomerOptionSet> listByOptionValue(
			CustomerOptionValue optionValue, MerchantStore store)
			throws ServiceException {
		return customerOptionSetRepository.findByOptionValueId(store.getId(), optionValue.getId());
	}


	




}
