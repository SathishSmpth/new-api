package com.kamatchibotique.application.model.catalog.marketplace;

import java.util.ArrayList;
import java.util.List;

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
public class Catalog extends Auditable<String> {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private MerchantStore store;
	
	private String code;
	
	private List<CatalogDescription> descriptions = new ArrayList<CatalogDescription>();
}
