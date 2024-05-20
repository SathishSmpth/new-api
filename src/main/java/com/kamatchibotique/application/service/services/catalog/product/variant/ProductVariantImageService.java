package com.kamatchibotique.application.service.services.catalog.product.variant;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariantImage;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface ProductVariantImageService extends CommonService<Long, ProductVariantImage> {

	
	List<ProductVariantImage> list(Long productVariantId, MerchantStore store);
	List<ProductVariantImage> listByProduct(Long productId, MerchantStore store);
	List<ProductVariantImage> listByProductVariantGroup(Long productVariantGroupId, MerchantStore store);
	
}
