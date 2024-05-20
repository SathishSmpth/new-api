package com.kamatchibotique.application.service.services.catalog.category;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import org.springframework.data.domain.Page;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.category.CategoryDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface CategoryService {

	List<Category> getListByLineage(MerchantStore store, String lineage) throws ServiceException;
	
	List<Category> listBySeUrl(MerchantStore store, String seUrl) throws ServiceException;
	
	CategoryDescription getDescription(Category category, Language language) throws ServiceException;

	void addCategoryDescription(Category category, CategoryDescription description) throws ServiceException;

	void addChild(Category parent, Category child) throws ServiceException;

	List<Category> listByParent(Category category) throws ServiceException;
	
	List<Category> listByStoreAndParent(MerchantStore store, Category category) throws ServiceException;
	
	
	List<Category> getByName(MerchantStore store, String name, Language language) throws ServiceException;
	
	List<Category> listByStore(MerchantStore store) throws ServiceException;

	Category getByCode(MerchantStore store, String code)
			throws ServiceException;

	List<Category> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	void saveOrUpdate(Category category) throws ServiceException;

	List<Category> getListByDepth(MerchantStore store, int depth);
	
	Category getById(Long id, int merchantId);
	
	Category getById(Long categoryid, int merchantId, int language);
	
	Page<Category> getListByDepth(MerchantStore store, Language language, String name, int depth, int page, int count);

	List<Category> getListByDepth(MerchantStore store, int depth, Language language);

	List<Category> getListByDepthFilterByFeatured(MerchantStore store, int depth, Language language);

	List<Category> getListByLineage(String storeCode, String lineage)
			throws ServiceException;

	Category getByCode(String storeCode, String code) throws ServiceException;
	
	Category getById(MerchantStore store, Long id) throws ServiceException;

	Category getBySeUrl(MerchantStore store, String seUrl, Language language);

	List<Category> listByParent(Category category, Language language);

	Category getOneByLanguage(long categoryId, Language language);

	List<Object[]> countProductsByCategories(MerchantStore store,
			List<Long> categoryIds) throws ServiceException;
	
	List<Category> getByProductId(Long productId, MerchantStore store);

	List<Category> listByCodes(MerchantStore store, List<String> codes,
			Language language);

	List<Category> listByIds(MerchantStore store, List<Long> ids,
			Language language);

	Category findById(Long category);
	
	int count(MerchantStore store);
}
