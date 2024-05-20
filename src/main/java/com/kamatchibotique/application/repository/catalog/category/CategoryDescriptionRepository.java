package com.kamatchibotique.application.repository.catalog.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kamatchibotique.application.model.catalog.category.CategoryDescription;


public interface CategoryDescriptionRepository extends JpaRepository<CategoryDescription, Long> {
	

	@Query("select c from CategoryDescription c where c.category.id = ?1")
	List<CategoryDescription> listByCategoryId(Long categoryId);
	



	
}
