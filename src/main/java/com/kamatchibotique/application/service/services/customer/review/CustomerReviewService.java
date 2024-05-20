package com.kamatchibotique.application.service.services.customer.review;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.customer.review.CustomerReview;

public interface CustomerReviewService extends
	CommonService<Long, CustomerReview> {
	List<CustomerReview> getByCustomer(Customer customer);
	List<CustomerReview> getByReviewedCustomer(Customer customer);
	CustomerReview getByReviewerAndReviewed(Long reviewer, Long reviewed);
}
