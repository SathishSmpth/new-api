package com.kamatchibotique.application.service.services.customer.attribute;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;



public interface CustomerAttributeService extends
		CommonService<Long, CustomerAttribute> {

	void saveOrUpdate(CustomerAttribute customerAttribute)
			throws ServiceException;

	CustomerAttribute getByCustomerOptionId(MerchantStore store,
			Long customerId, Long id);

	List<CustomerAttribute> getByCustomerOptionValueId(MerchantStore store,
			Long id);

	List<CustomerAttribute> getByOptionId(MerchantStore store, Long id);


	List<CustomerAttribute> getByCustomer(MerchantStore store, Customer customer);
	

}
