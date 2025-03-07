package com.kamatchibotique.application.repository.content;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kamatchibotique.application.model.content.Content;
import com.kamatchibotique.application.enums.content.ContentType;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface PageContentRepository extends PagingAndSortingRepository<MerchantStore, Long> {
	
	

	@Query(value = "select c from Content c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType = ?1 and cm.id = ?2 order by c.sortOrder asc",
      countQuery = "select count(distinct c) from Content c join c.merchantStore cm where c.contentType = ?1 and cm.id = ?2")
	Page<Content> findByContentType(ContentType contentType, Integer storeId, Pageable pageable);
	
	@Query(value = "select c from Content c left join fetch c.descriptions cd join fetch c.merchantStore cm join fetch cd.language cdl where c.contentType = ?1 and cm.id = ?2 and cdl.id = ?3 order by c.sortOrder asc",
		      countQuery = "select count(distinct c) from Content c join c.merchantStore cm where c.contentType = ?1 and cm.id = ?2")
			Page<Content> findByContentType(ContentType contentTypes, Integer storeId, Integer language, Pageable pageable);
	
	

}
