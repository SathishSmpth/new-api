package com.kamatchibotique.application.service.impl.catalog.product.price;



import java.util.List;

import com.kamatchibotique.application.service.services.catalog.product.price.ProductPriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.price.ProductPriceRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.price.ProductPrice;
import com.kamatchibotique.application.model.catalog.product.price.ProductPriceDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;

@Service("productPrice")
public class ProductPriceServiceImpl extends CommonServiceImpl<Long, ProductPrice> 
	implements ProductPriceService {
	
	private ProductPriceRepository productPriceRepository;

	@Autowired
	public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository) {
		super(productPriceRepository);
		this.productPriceRepository = productPriceRepository;
	}

	@Override
	public void addDescription(ProductPrice price,
			ProductPriceDescription description) throws ServiceException {
		price.getDescriptions().add(description);
		update(price);
	}
	
	
	@Override
	public ProductPrice saveOrUpdate(ProductPrice price) throws ServiceException {
		
		
		ProductPrice returnEntity = productPriceRepository.save(price);

		return returnEntity;


	}
	
	@Override
	public void delete(ProductPrice price) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached variant
		price = this.getById(price.getId());
		super.delete(price);
		
	}

	@Override
	public List<ProductPrice> findByProductSku(String sku, MerchantStore store) {

		return productPriceRepository.findByProduct(sku, store.getCode());
	}

	@Override
	public ProductPrice findById(Long priceId, String sku, MerchantStore store) {
		
		return productPriceRepository.findByProduct(sku, priceId, store.getCode());
	}

	@Override
	public List<ProductPrice> findByInventoryId(Long productInventoryId, String sku, MerchantStore store) {

		return productPriceRepository.findByProductInventoty(sku, productInventoryId, store.getCode());
	}
	


}
