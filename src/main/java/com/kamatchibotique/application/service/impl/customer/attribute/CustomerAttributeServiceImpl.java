package com.kamatchibotique.application.service.impl.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.customer.attribute.CustomerAttributeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.attribute.CustomerAttributeRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;



@Service("customerAttributeService")
public class CustomerAttributeServiceImpl extends
		CommonServiceImpl<Long, CustomerAttribute> implements CustomerAttributeService {
	
	private CustomerAttributeRepository customerAttributeRepository;

	@Autowired
	public CustomerAttributeServiceImpl(CustomerAttributeRepository customerAttributeRepository) {
		super(customerAttributeRepository);
		this.customerAttributeRepository = customerAttributeRepository;
	}
	




	@Override
	public void saveOrUpdate(CustomerAttribute customerAttribute)
			throws ServiceException {

			customerAttributeRepository.save(customerAttribute);

		
	}
	
	@Override
	public void delete(CustomerAttribute attribute) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached instance
		attribute = this.getById(attribute.getId());
		super.delete(attribute);
		
	}
	


	@Override
	public CustomerAttribute getByCustomerOptionId(MerchantStore store, Long customerId, Long id) {
		return customerAttributeRepository.findByOptionId(store.getId(), customerId, id);
	}



	@Override
	public List<CustomerAttribute> getByCustomer(MerchantStore store, Customer customer) {
		return customerAttributeRepository.findByCustomerId(store.getId(), customer.getId());
	}


	@Override
	public List<CustomerAttribute> getByCustomerOptionValueId(MerchantStore store,
			Long id) {
		return customerAttributeRepository.findByOptionValueId(store.getId(), id);
	}
	
	@Override
	public List<CustomerAttribute> getByOptionId(MerchantStore store,
			Long id) {
		return customerAttributeRepository.findByOptionId(store.getId(), id);
	}

}
