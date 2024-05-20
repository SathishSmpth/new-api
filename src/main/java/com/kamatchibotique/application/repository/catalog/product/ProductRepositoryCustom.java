package com.kamatchibotique.application.repository.catalog.product;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.ProductCriteria;
import com.kamatchibotique.application.model.catalog.product.ProductList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;

public interface ProductRepositoryCustom {


		ProductList listByStore(MerchantStore store, Language language,
			ProductCriteria criteria);

		Product getProductWithOnlyMerchantStoreById(Long productId);

		 Product getByFriendlyUrl(MerchantStore store,String seUrl, Locale locale);

		List<Product> getProductsListByCategories(@SuppressWarnings("rawtypes") Set categoryIds);

		List<Product> getProductsListByCategories(Set<Long> categoryIds,
				Language language);

		List<Product> getProductsListByIds(Set<Long> productIds);

		List<Product> listByTaxClass(TaxClass taxClass);

		List<Product> listByStore(MerchantStore store);

		Product getProductForLocale(long productId, Language language,
				Locale locale);

		Product getById(Long productId);
		Product getById(Long productId, MerchantStore merchant);

		@Deprecated
		Product getByCode(String productCode, Language language);

		@Deprecated
		Product getByCode(String productCode, MerchantStore store);
		
		Product getById(Long productId, MerchantStore store, Language language);

		List<Product> getProductsForLocale(MerchantStore store,
				Set<Long> categoryIds, Language language, Locale locale);

}
