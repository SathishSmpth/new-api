package com.kamatchibotique.application.service.services.catalog.product.file;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.file.DigitalProduct;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;


public interface DigitalProductService extends CommonService<Long, DigitalProduct> {

	void saveOrUpdate(DigitalProduct digitalProduct) throws ServiceException;

	void addProductFile(Product product, DigitalProduct digitalProduct,
			InputContentFile inputFile) throws ServiceException;



	DigitalProduct getByProduct(MerchantStore store, Product product)
			throws ServiceException;

	
}
