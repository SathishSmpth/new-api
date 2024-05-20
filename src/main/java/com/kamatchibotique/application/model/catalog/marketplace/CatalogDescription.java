package com.kamatchibotique.application.model.catalog.marketplace;


import com.kamatchibotique.application.model.common.description.Description;
import com.kamatchibotique.application.model.reference.language.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class CatalogDescription extends Description {

	private static final long serialVersionUID = 1L;

	private Catalog catalog;

	public CatalogDescription() {
	}
	
	public CatalogDescription(String name, Language language) {
		this.setName(name);
		this.setLanguage(language);
		super.setId(0L);
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
}
