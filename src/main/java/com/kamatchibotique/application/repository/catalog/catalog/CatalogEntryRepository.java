package com.kamatchibotique.application.repository.catalog.catalog;

import com.kamatchibotique.application.model.catalog.catalog.CatalogCategoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface CatalogEntryRepository extends JpaRepository<CatalogCategoryEntry, Long>, QuerydslPredicateExecutor<CatalogCategoryEntry>{

}
