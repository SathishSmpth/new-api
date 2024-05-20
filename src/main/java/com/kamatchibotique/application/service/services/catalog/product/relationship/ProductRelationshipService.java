package com.kamatchibotique.application.service.services.catalog.product.relationship;

import java.util.List;

import com.kamatchibotique.application.enums.product.ProductRelationshipType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.relationship.ProductRelationship;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductRelationshipService extends
		CommonService<Long, ProductRelationship> {

	void saveOrUpdate(ProductRelationship relationship) throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store, Product product,
										ProductRelationshipType type, Language language) throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store, Product product,
			String name) throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store, Product product,
			ProductRelationshipType type)
			throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store,
			ProductRelationshipType type) throws ServiceException;

	List<ProductRelationship> listByProduct(Product product)
			throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store,
			ProductRelationshipType type, Language language)
			throws ServiceException;

	List<ProductRelationship> getGroups(MerchantStore store);

	List<ProductRelationship> getGroupDefinition(MerchantStore store, String name);

	void addGroup(MerchantStore store, String groupName) throws ServiceException;

	List<ProductRelationship> getByGroup(MerchantStore store, String groupName)
			throws ServiceException;

	void deleteGroup(MerchantStore store, String groupName)
			throws ServiceException;

	void deactivateGroup(MerchantStore store, String groupName)
			throws ServiceException;
	
	void deleteRelationship(ProductRelationship relationship) throws ServiceException;

	void activateGroup(MerchantStore store, String groupName)
			throws ServiceException;

	List<ProductRelationship> getByGroup(MerchantStore store, String groupName,
			Language language) throws ServiceException;

}
