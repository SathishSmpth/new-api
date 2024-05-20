package com.kamatchibotique.application.service.impl.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.customer.attribute.CustomerAttributeService;
import com.kamatchibotique.application.service.services.customer.attribute.CustomerOptionSetService;
import com.kamatchibotique.application.service.services.customer.attribute.CustomerOptionValueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.attribute.CustomerOptionValueRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionSet;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionValue;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


@Service("customerOptionValueService")
public class CustomerOptionValueServiceImpl extends
		CommonServiceImpl<Long, CustomerOptionValue> implements
        CustomerOptionValueService {

	@Autowired
	private CustomerAttributeService customerAttributeService;
	
	private CustomerOptionValueRepository customerOptionValueRepository;
	
	@Autowired
	private CustomerOptionSetService customerOptionSetService;
	
	@Autowired
	public CustomerOptionValueServiceImpl(
			CustomerOptionValueRepository customerOptionValueRepository) {
			super(customerOptionValueRepository);
			this.customerOptionValueRepository = customerOptionValueRepository;
	}
	
	
	@Override
	public List<CustomerOptionValue> listByStore(MerchantStore store, Language language) throws ServiceException {
		
		return customerOptionValueRepository.findByStore(store.getId(), language.getId());
	}
	


	
	@Override
	public void saveOrUpdate(CustomerOptionValue entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {

			super.update(entity);
			
		} else {
			
			super.save(entity);
			
		}
		
	}
	
	
	public void delete(CustomerOptionValue customerOptionValue) throws ServiceException {
		
		//remove all attributes having this option
		List<CustomerAttribute> attributes = customerAttributeService.getByCustomerOptionValueId(customerOptionValue.getMerchantStore(), customerOptionValue.getId());
		
		for(CustomerAttribute attribute : attributes) {
			customerAttributeService.delete(attribute);
		}
		
		List<CustomerOptionSet> optionSets = customerOptionSetService.listByOptionValue(customerOptionValue, customerOptionValue.getMerchantStore());
		
		for(CustomerOptionSet optionSet : optionSets) {
			customerOptionSetService.delete(optionSet);
		}
		
		CustomerOptionValue option = super.getById(customerOptionValue.getId());
		
		//remove option
		super.delete(option);
		
	}
	
	@Override
	public CustomerOptionValue getByCode(MerchantStore store, String optionValueCode) {
		return customerOptionValueRepository.findByCode(store.getId(), optionValueCode);
	}



}
