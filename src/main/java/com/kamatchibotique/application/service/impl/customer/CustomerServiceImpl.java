package com.kamatchibotique.application.service.impl.customer;

import java.util.List;

import com.kamatchibotique.application.service.services.customer.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.CustomerRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.customer.attribute.CustomerAttributeService;
import com.kamatchibotique.application.model.common.Address;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.CustomerCriteria;
import com.kamatchibotique.application.model.customer.CustomerList;
import com.kamatchibotique.application.model.customer.attribute.CustomerAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.utils.GeoLocation;



@Service("customerService")
public class CustomerServiceImpl extends CommonServiceImpl<Long, Customer> implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerAttributeService customerAttributeService;
	
	@Autowired
	private GeoLocation geoLocation;

	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		super(customerRepository);
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> getByName(String firstName) {
		return customerRepository.findByName(firstName);
	}
	
	@Override
	public Customer getById(Long id) {
			return customerRepository.findOne(id);		
	}
	
	@Override
	public Customer getByNick(String nick) {
		return customerRepository.findByNick(nick);	
	}
	
	@Override
	public Customer getByNick(String nick, int storeId) {
		return customerRepository.findByNick(nick, storeId);	
	}
	
	@Override
	public List<Customer> getListByStore(MerchantStore store) {
		return customerRepository.findByStore(store.getId());
	}
	
	@Override
	public CustomerList getListByStore(MerchantStore store, CustomerCriteria criteria) {
		return customerRepository.listByStore(store,criteria);
	}
	
	@Override
	public Address getCustomerAddress(MerchantStore store, String ipAddress) throws ServiceException {
		
		try {
			return geoLocation.getAddress(ipAddress);
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override	
	public void saveOrUpdate(Customer customer) throws ServiceException {

		LOGGER.debug("Creating Customer");
		
		if(customer.getId()!=null && customer.getId()>0) {
			super.update(customer);
		} else {			
		
			super.create(customer);

		}
	}

	public void delete(Customer customer) throws ServiceException {
		customer = getById(customer.getId());
		
		//delete attributes
		List<CustomerAttribute> attributes =customerAttributeService.getByCustomer(customer.getMerchantStore(), customer);
		if(attributes!=null) {
			for(CustomerAttribute attribute : attributes) {
				customerAttributeService.delete(attribute);
			}
		}
		customerRepository.delete(customer);

	}

	@Override
	public Customer getByNick(String nick, String code) {
		return customerRepository.findByNick(nick, code);
	}

	@Override
	public Customer getByPasswordResetToken(String storeCode, String token) {
		return customerRepository.findByResetPasswordToken(token, storeCode);
	}
	

}
