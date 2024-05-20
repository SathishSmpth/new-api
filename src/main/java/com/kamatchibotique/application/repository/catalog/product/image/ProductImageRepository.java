package com.kamatchibotique.application.repository.catalog.product.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kamatchibotique.application.model.catalog.product.image.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {


	@Query("select p from ProductImage p left join fetch p.descriptions pd inner join fetch p.product pp inner join fetch pp.merchantStore ppm where p.id = ?1")
	ProductImage findOne(Long id);
	
	@Query("select p from ProductImage p left join fetch p.descriptions pd inner join fetch p.product pp inner join fetch pp.merchantStore ppm where pp.id = ?2 and ppm.code = ?3 and p.id = ?1")
	ProductImage finById(Long imageId, Long productId, String storeCode);
	
	
}
