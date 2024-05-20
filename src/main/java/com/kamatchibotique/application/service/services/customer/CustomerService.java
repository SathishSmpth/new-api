package com.kamatchibotique.application.service.services.customer;


import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.common.Address;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.CustomerCriteria;
import com.kamatchibotique.application.model.customer.CustomerList;
import com.kamatchibotique.application.model.merchant.MerchantStore;



public interface CustomerService  extends CommonService<Long, Customer> {

	List<Customer> getByName(String firstName);

	List<Customer> getListByStore(MerchantStore store);

	Customer getByNick(String nick);

	void saveOrUpdate(Customer customer) throws ServiceException ;

	CustomerList getListByStore(MerchantStore store, CustomerCriteria criteria);

	Customer getByNick(String nick, int storeId);
	Customer getByNick(String nick, String code);

	Customer getByPasswordResetToken(String storeCode, String token);

	Address getCustomerAddress(MerchantStore store, String ipAddress)
			throws ServiceException;
}
