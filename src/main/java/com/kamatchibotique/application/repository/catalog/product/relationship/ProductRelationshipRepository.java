package com.kamatchibotique.application.repository.catalog.product.relationship;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.catalog.product.relationship.ProductRelationship;


public interface ProductRelationshipRepository extends JpaRepository<ProductRelationship, Long>, ProductRelationshipRepositoryCustom {

}
