package com.kamatchibotique.application.model.catalog.product;

import java.util.List;

import com.kamatchibotique.application.model.catalog.product.attribute.AttributeCriteria;
import com.kamatchibotique.application.model.common.Criteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCriteria extends Criteria {
	
	public static final String ORIGIN_SHOP = "shop";
	public static final String ORIGIN_ADMIN = "admin";
	
	private String productName;
	private List<AttributeCriteria> attributeCriteria;
	private String origin = ORIGIN_SHOP;

	
	private Boolean available = null;
	
	private List<Long> categoryIds;
	private List<String> availabilities;
	private List<Long> productIds;
	private List<Long> optionValueIds;
	private String sku;
	
	//V2
	private List<String> optionValueCodes;
	private String option;
	
	private String status;
	
	private Long manufacturerId = null;
	
	private Long ownerId = null;
}
