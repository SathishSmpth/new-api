package com.kamatchibotique.application.service.services.catalog.product.review;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.review.ProductReview;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductReviewService extends
		CommonService<Long, ProductReview> {
	List<ProductReview> getByCustomer(Customer customer);
	List<ProductReview> getByProduct(Product product);
	List<ProductReview> getByProduct(Product product, Language language);
	ProductReview getByProductAndCustomer(Long productId, Long customerId);
	List<ProductReview> getByProductNoCustomers(Product product);
}
