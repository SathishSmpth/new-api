package com.kamatchibotique.application.repository.catalog.product.relationship;

import java.util.List;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.relationship.ProductRelationship;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


public interface ProductRelationshipRepositoryCustom {

	List<ProductRelationship> getByType(MerchantStore store, String type,
			Language language);

	List<ProductRelationship> getByType(MerchantStore store, String type,
			Product product, Language language);

	List<ProductRelationship> getByGroup(MerchantStore store, String group);

	List<ProductRelationship> getGroups(MerchantStore store);

	List<ProductRelationship> getByType(MerchantStore store, String type);
	
	List<ProductRelationship> getGroupByType(MerchantStore store, String type);

	List<ProductRelationship> listByProducts(Product product);

	List<ProductRelationship> getByType(MerchantStore store, String type,
			Product product);
	
	List<ProductRelationship> getByTypeAndRelatedProduct(MerchantStore store, String type,
			Product product);

}
