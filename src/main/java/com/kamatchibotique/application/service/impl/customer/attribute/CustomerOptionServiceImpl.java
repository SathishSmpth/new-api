package com.kamatchibotique.application.service.impl.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.customer.attribute.CustomerAttributeService;
import com.kamatchibotique.application.service.services.customer.attribute.CustomerOptionService;
import com.kamatchibotique.application.service.services.customer.attribute.CustomerOptionSetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.attribute.CustomerOptionRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.customer.attribute.CustomerOption;
import com.kamatchibotique.application.model.customer.attribute.CustomerOptionSet;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;



@Service("customerOptionService")
public class CustomerOptionServiceImpl extends
		CommonServiceImpl<Long, CustomerOption> implements CustomerOptionService {

	
	private CustomerOptionRepository customerOptionRepository;
	
	@Autowired
	private CustomerAttributeService customerAttributeService;
	
	@Autowired
	private CustomerOptionSetService customerOptionSetService;
	

	@Autowired
	public CustomerOptionServiceImpl(
			CustomerOptionRepository customerOptionRepository) {
			super(customerOptionRepository);
			this.customerOptionRepository = customerOptionRepository;
	}
	
	@Override
	public List<CustomerOption> listByStore(MerchantStore store, Language language) throws ServiceException {

		return customerOptionRepository.findByStore(store.getId(), language.getId());

	}
	

	@Override
	public void saveOrUpdate(CustomerOption entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {
			super.update(entity);
		} else {
			super.save(entity);
		}
		
	}


	@Override
	public void delete(CustomerOption customerOption) throws ServiceException {
		
		//remove all attributes having this option
		List<CustomerAttribute> attributes = customerAttributeService.getByOptionId(customerOption.getMerchantStore(), customerOption.getId());
		
		for(CustomerAttribute attribute : attributes) {
			customerAttributeService.delete(attribute);
		}
		
		CustomerOption option = this.getById(customerOption.getId());
		
		List<CustomerOptionSet> optionSets = customerOptionSetService.listByOption(customerOption, customerOption.getMerchantStore());
		
		for(CustomerOptionSet optionSet : optionSets) {
			customerOptionSetService.delete(optionSet);
		}
		
		//remove option
		super.delete(option);
		
	}
	
	@Override
	public CustomerOption getByCode(MerchantStore store, String optionCode) {
		return customerOptionRepository.findByCode(store.getId(), optionCode);
	}
	

	




}
