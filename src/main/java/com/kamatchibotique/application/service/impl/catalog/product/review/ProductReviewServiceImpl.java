package com.kamatchibotique.application.service.impl.catalog.product.review;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.review.ProductReview;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.catalog.product.review.ProductReviewRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.ProductService;
import com.kamatchibotique.application.service.services.catalog.product.review.ProductReviewService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("productReviewService")
public class ProductReviewServiceImpl extends
        CommonServiceImpl<Long, ProductReview> implements
        ProductReviewService {


    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    public ProductReviewServiceImpl(
            ProductReviewRepository productReviewRepository) {
        super(productReviewRepository);
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public List<ProductReview> getByCustomer(Customer customer) {
        return productReviewRepository.findByCustomer(customer.getId());
    }

    @Override
    public List<ProductReview> getByProduct(Product product) {
        return productReviewRepository.findByProduct(product.getId());
    }

    @Override
    public ProductReview getByProductAndCustomer(Long productId, Long customerId) {
        return productReviewRepository.findByProductAndCustomer(productId, customerId);
    }

    @Override
    public List<ProductReview> getByProduct(Product product, Language language) {
        return productReviewRepository.findByProduct(product.getId(), language.getId());
    }

    private void saveOrUpdate(ProductReview review) throws ServiceException {


        Validate.notNull(review, "ProductReview cannot be null");
        Validate.notNull(review.getProduct(), "ProductReview.product cannot be null");
        Validate.notNull(review.getCustomer(), "ProductReview.customer cannot be null");


        //refresh product
        Product product = productService.getById(review.getProduct().getId());

        //ajust product rating
        Integer count = 0;
        if (product.getProductReviewCount() != null) {
            count = product.getProductReviewCount();
        }


        BigDecimal averageRating = product.getProductReviewAvg();
        if (averageRating == null) {
            averageRating = new BigDecimal(0);
        }
        //get reviews


        BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
        totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));

        count = count + 1;
        double avg = totalRating.doubleValue() / count;

        product.setProductReviewAvg(new BigDecimal(avg));
        product.setProductReviewCount(count);
        super.save(review);

        productService.update(product);

        review.setProduct(product);

    }

    public void update(ProductReview review) throws ServiceException {
        this.saveOrUpdate(review);
    }

    public void create(ProductReview review) throws ServiceException {
        this.saveOrUpdate(review);
    }

    @Override
    public List<ProductReview> getByProductNoCustomers(Product product) {
        return productReviewRepository.findByProductNoCustomers(product.getId());
    }
}
