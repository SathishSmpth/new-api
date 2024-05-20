package com.kamatchibotique.application.model.common;

import java.util.List;

import com.kamatchibotique.application.enums.CriteriaOrderBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Criteria {
	// legacy pagination
	private int startIndex = 0;
	private int maxCount = 0;
	// new pagination
	private int startPage = 0;
	private int pageSize = 10;
	private boolean legacyPagination = true;
	private String code;
	private String name;
	private String language;
	private String user;
	private String storeCode;
	private List<Integer> storeIds;

	private CriteriaOrderBy orderBy = CriteriaOrderBy.DESC;
	private String criteriaOrderByField;
	private String search;
}