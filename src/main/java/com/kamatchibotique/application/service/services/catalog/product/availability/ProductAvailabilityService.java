package com.kamatchibotique.application.service.services.catalog.product.availability;

import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface ProductAvailabilityService extends
		CommonService<Long, ProductAvailability> {

	ProductAvailability saveOrUpdate(ProductAvailability availability) throws ServiceException;
	
	Page<ProductAvailability> listByProduct(Long productId, MerchantStore store, int page, int count);
	
	/**
	 * Get by product sku and store
	 * @param sku
	 * @param store
	 * @return
	 */
	Page<ProductAvailability> getBySku(String sku, MerchantStore store, int page, int count);
	
	
	/**
	 * Get by sku
	 * @param sku
	 * @return
	 */
	Page<ProductAvailability> getBySku(String sku, int page, int count);
	
	/**
	 * All availability by product / product variant sku and store
	 * @param sku
	 * @param store
	 * @return
	 */
	List<ProductAvailability> getBySku(String sku, MerchantStore store);

	Optional<ProductAvailability> getById(Long availabilityId, MerchantStore store);


}
