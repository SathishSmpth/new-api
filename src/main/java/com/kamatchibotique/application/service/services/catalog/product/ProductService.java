package com.kamatchibotique.application.service.services.catalog.product;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.ProductCriteria;
import com.kamatchibotique.application.model.catalog.product.ProductList;
import com.kamatchibotique.application.model.catalog.product.description.ProductDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;



public interface ProductService extends CommonService<Long, Product> {

	Optional<Product> retrieveById(Long id, MerchantStore store);

	void addProductDescription(Product product, ProductDescription description) throws ServiceException;

	ProductDescription getProductDescription(Product product, Language language);

	Product getProductForLocale(long productId, Language language, Locale locale) throws ServiceException;

	List<Product> getProductsForLocale(Category category, Language language, Locale locale) throws ServiceException;

	List<Product> getProducts(List<Long> categoryIds) throws ServiceException;

	List<Product> getProductsByIds(List<Long> productIds) throws ServiceException;

	Product saveProduct(Product product) throws ServiceException;

	Product getProductWithOnlyMerchantStoreById(Long productId);

	ProductList listByStore(MerchantStore store, Language language,
			ProductCriteria criteria);
	
	boolean exists(String sku, MerchantStore store);

	Page<Product> listByStore(MerchantStore store, Language language,
			ProductCriteria criteria, int page, int count);

	List<Product> listByStore(MerchantStore store);

	List<Product> listByTaxClass(TaxClass taxClass);

	List<Product> getProducts(List<Long> categoryIds, Language language)
			throws ServiceException;

	Product getBySeUrl(MerchantStore store, String seUrl, Locale locale);

	Product getBySku(String productCode, MerchantStore merchant, Language language) throws ServiceException;
	
	
	Product getBySku(String productCode, MerchantStore merchant) throws ServiceException;

	Product findOne(Long id, MerchantStore merchant);
}

