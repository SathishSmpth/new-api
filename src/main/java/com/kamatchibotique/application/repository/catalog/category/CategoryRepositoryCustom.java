package com.kamatchibotique.application.repository.catalog.category;

import java.util.List;

import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface CategoryRepositoryCustom {

	List<Object[]> countProductsByCategories(MerchantStore store,
			List<Long> categoryIds);

	List<Category> listByStoreAndParent(MerchantStore store, Category category);
	
	List<Category> listByProduct(MerchantStore store, Long product);

}
