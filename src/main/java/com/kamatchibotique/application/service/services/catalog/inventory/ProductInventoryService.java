package com.kamatchibotique.application.service.services.catalog.inventory;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.inventory.ProductInventory;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;

public interface ProductInventoryService {
	
	
	ProductInventory inventory(Product product) throws ServiceException;
	ProductInventory inventory(ProductVariant variant) throws ServiceException;

}
