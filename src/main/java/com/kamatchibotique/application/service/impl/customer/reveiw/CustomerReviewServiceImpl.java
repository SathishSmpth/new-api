package com.kamatchibotique.application.service.impl.customer.reveiw;

import java.math.BigDecimal;
import java.util.List;

import com.kamatchibotique.application.service.services.customer.review.CustomerReviewService;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.customer.review.CustomerReviewRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.customer.CustomerService;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.review.CustomerReview;

@Service("customerReviewService")
public class CustomerReviewServiceImpl extends
	CommonServiceImpl<Long, CustomerReview> implements CustomerReviewService {
	
	private CustomerReviewRepository customerReviewRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	public CustomerReviewServiceImpl(
			CustomerReviewRepository customerReviewRepository) {
			super(customerReviewRepository);
			this.customerReviewRepository = customerReviewRepository;
	}
	
	
	private void saveOrUpdate(CustomerReview review) throws ServiceException {
		

		Validate.notNull(review,"CustomerReview cannot be null");
		Validate.notNull(review.getCustomer(),"CustomerReview.customer cannot be null");
		Validate.notNull(review.getReviewedCustomer(),"CustomerReview.reviewedCustomer cannot be null");
		
		
		//refresh customer
		Customer customer = customerService.getById(review.getReviewedCustomer().getId());
		
		//ajust product rating
		Integer count = 0;
		if(customer.getCustomerReviewCount()!=null) {
			count = customer.getCustomerReviewCount();
		}
				
		
		

		BigDecimal averageRating = customer.getCustomerReviewAvg();
		if(averageRating==null) {
			averageRating = new BigDecimal(0);
		}
		//get reviews

		
		BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
		totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));
		
		count = count + 1;
		double avg = totalRating.doubleValue() / count;
		
		customer.setCustomerReviewAvg(new BigDecimal(avg));
		customer.setCustomerReviewCount(count);
		super.save(review);
		
		customerService.update(customer);
		
		review.setReviewedCustomer(customer);

		
	}
	
	public void update(CustomerReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}
	
	public void create(CustomerReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}
	
	

	@Override
	public List<CustomerReview> getByCustomer(Customer customer) {
		Validate.notNull(customer,"Customer cannot be null");
		return customerReviewRepository.findByReviewer(customer.getId());
	}

	@Override
	public List<CustomerReview> getByReviewedCustomer(Customer customer) {
		Validate.notNull(customer,"Customer cannot be null");
		return customerReviewRepository.findByReviewed(customer.getId());
	}


	@Override
	public CustomerReview getByReviewerAndReviewed(Long reviewer, Long reviewed) {
		Validate.notNull(reviewer,"Reviewer customer cannot be null");
		Validate.notNull(reviewed,"Reviewer customer cannot be null");
		return customerReviewRepository.findByRevieweAndReviewed(reviewer, reviewed);
	}

}
