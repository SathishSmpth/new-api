package com.kamatchibotique.application.model.catalog.marketplace;

import java.util.HashSet;
import java.util.Set;

import com.kamatchibotique.application.model.catalog.catalog.Catalog;
import com.kamatchibotique.application.model.common.Auditable;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketPlace extends Auditable<String> {
	private static final long serialVersionUID = 1L;

	private MerchantStore store;
	
	private Long id;
	
	private String code;
	
	private Set<Catalog> catalogs = new HashSet<Catalog>();
}
