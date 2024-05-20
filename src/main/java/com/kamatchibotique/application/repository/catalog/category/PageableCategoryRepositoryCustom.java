package com.kamatchibotique.application.repository.catalog.category;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kamatchibotique.application.model.catalog.category.Category;

public interface PageableCategoryRepositoryCustom {
	
	Page<Category> listByStore(Integer storeId, Integer languageId, String name, Pageable pageable);

}
