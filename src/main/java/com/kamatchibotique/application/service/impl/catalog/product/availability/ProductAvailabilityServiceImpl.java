package com.kamatchibotique.application.service.impl.catalog.product.availability;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;

import com.kamatchibotique.application.service.services.catalog.product.availability.ProductAvailabilityService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.availability.PageableProductAvailabilityRepository;
import com.kamatchibotique.application.repository.catalog.product.availability.ProductAvailabilityRepository;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.merchant.MerchantStore;

@Service("productAvailabilityService")
public class ProductAvailabilityServiceImpl extends CommonServiceImpl<Long, ProductAvailability>
		implements ProductAvailabilityService {

	@Autowired
	private ProductAvailabilityRepository productAvailabilityRepository;

	@Autowired
	private PageableProductAvailabilityRepository pageableProductAvailabilityRepository;

	@Autowired
	public ProductAvailabilityServiceImpl(ProductAvailabilityRepository productAvailabilityRepository) {
		super(productAvailabilityRepository);
		this.productAvailabilityRepository = productAvailabilityRepository;
	}

	@Override
	public ProductAvailability saveOrUpdate(ProductAvailability availability) throws ServiceException {
		if (isPositive(availability.getId())) {
			update(availability);
		} else {
			create(availability);
		}
		
		return availability;
	}

	private boolean isPositive(Long id) {
		return Objects.nonNull(id) && id > 0;
	}



	@Override
	public Page<ProductAvailability> listByProduct(Long productId, MerchantStore store, int page,
			int count) {
		Validate.notNull(productId, "Product cannot be null");
		Validate.notNull(store, "MercantStore cannot be null");
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductAvailabilityRepository.getByProductId(productId, store.getCode(), pageRequest);
	}



	@Override
	public Optional<ProductAvailability> getById(Long availabilityId, MerchantStore store) {
		Validate.notNull(store, "Merchant must not be null");
		return Optional.ofNullable(productAvailabilityRepository.getById(availabilityId));
	}

	@Override
	public Page<ProductAvailability> getBySku(String sku, MerchantStore store, int page, int count) {
		Validate.notNull(store, "MerchantStore cannot be null");
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductAvailabilityRepository.getBySku(sku, store.getCode(), pageRequest);
	}

	@Override
	public Page<ProductAvailability> getBySku(String sku, int page, int count) {
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductAvailabilityRepository.getBySku(sku, pageRequest);
	}

	@Override
	public List<ProductAvailability> getBySku(String sku, MerchantStore store) {
		Validate.notNull(store, "MerchantStore cannot be null");
		return productAvailabilityRepository.getBySku(sku, store.getCode());
	}

}
