package com.kamatchibotique.application.service.services.catalog.product.price;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.price.ProductPrice;
import com.kamatchibotique.application.model.catalog.product.price.ProductPriceDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface ProductPriceService extends CommonService<Long, ProductPrice> {

	void addDescription(ProductPrice price, ProductPriceDescription description) throws ServiceException;

	ProductPrice saveOrUpdate(ProductPrice price) throws ServiceException;
	
	List<ProductPrice> findByProductSku(String sku, MerchantStore store);
	
	ProductPrice findById(Long priceId, String sku, MerchantStore store);
	
	List<ProductPrice> findByInventoryId(Long productInventoryId, String sku, MerchantStore store);
	

}
