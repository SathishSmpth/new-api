package com.kamatchibotique.application.service.impl.catalog.product.relationShip;

import java.util.List;

import com.kamatchibotique.application.enums.product.ProductRelationshipType;
import com.kamatchibotique.application.service.services.catalog.product.relationship.ProductRelationshipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.relationship.ProductRelationshipRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.relationship.ProductRelationship;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

@Service("productRelationshipService")
public class ProductRelationshipServiceImpl extends
		CommonServiceImpl<Long, ProductRelationship> implements
        ProductRelationshipService {

	
	private ProductRelationshipRepository productRelationshipRepository;
	
	@Autowired
	public ProductRelationshipServiceImpl(
			ProductRelationshipRepository productRelationshipRepository) {
			super(productRelationshipRepository);
			this.productRelationshipRepository = productRelationshipRepository;
	}
	
	@Override
	public void saveOrUpdate(ProductRelationship relationship) throws ServiceException {
		
		if(relationship.getId()!=null && relationship.getId()>0) {
			
			this.update(relationship);
			
		} else {
			this.create(relationship);
		}
		
	}
	
	
	@Override
	public void addGroup(MerchantStore store, String groupName) throws ServiceException {
		ProductRelationship relationship = new ProductRelationship();
		relationship.setCode(groupName);
		relationship.setStore(store);
		relationship.setActive(true);
		this.save(relationship);
	}
	
	@Override
	public List<ProductRelationship> getGroups(MerchantStore store) {
		return productRelationshipRepository.getGroups(store);
	}
	
	@Override
	public void deleteGroup(MerchantStore store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = productRelationshipRepository.getByGroup(store, groupName);
		for(ProductRelationship relation : entities) {
			this.delete(relation);
		}
	}
	
	@Override
	public void deactivateGroup(MerchantStore store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = getGroupDefinition(store, groupName);
		for(ProductRelationship relation : entities) {
			relation.setActive(false);
			this.saveOrUpdate(relation);
		}
	}
	
	@Override
	public void activateGroup(MerchantStore store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = getGroupDefinition(store, groupName);
		for(ProductRelationship relation : entities) {
			relation.setActive(true);
			this.saveOrUpdate(relation);
		}
	}
	
	public void deleteRelationship(ProductRelationship relationship)  throws ServiceException {
		
		//throws detached exception so need to query first
		relationship = this.getById(relationship.getId());
		if(relationship != null) {
			delete(relationship);
		}
		
		
		
	}
	
	@Override
	public List<ProductRelationship> listByProduct(Product product) throws ServiceException {

		return productRelationshipRepository.listByProducts(product);

	}
	
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, Product product, ProductRelationshipType type, Language language) throws ServiceException {

		return productRelationshipRepository.getByType(store, type.name(), product, language);

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, ProductRelationshipType type, Language language) throws ServiceException {
		return productRelationshipRepository.getByType(store, type.name(), language);
	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, ProductRelationshipType type) throws ServiceException {

		return productRelationshipRepository.getByType(store, type.name());

	}
	
	@Override
	public List<ProductRelationship> getByGroup(MerchantStore store, String groupName) throws ServiceException {

		return productRelationshipRepository.getByType(store, groupName);

	}
	
	@Override
	public List<ProductRelationship> getByGroup(MerchantStore store, String groupName, Language language) throws ServiceException {

		return productRelationshipRepository.getByType(store, groupName, language);

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, Product product, ProductRelationshipType type) throws ServiceException {
		

		return productRelationshipRepository.getByType(store, type.name(), product);
				
		
	}

	@Override
	public List<ProductRelationship> getGroupDefinition(MerchantStore store, String name) {
		return productRelationshipRepository.getByGroup(store, name);
	}

	@Override
	public List<ProductRelationship> getByType(MerchantStore store, Product product, String name)
			throws ServiceException {
		return productRelationshipRepository.getByTypeAndRelatedProduct(store, name, product);
	}



}
