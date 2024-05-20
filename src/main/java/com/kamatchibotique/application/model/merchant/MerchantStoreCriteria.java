package com.kamatchibotique.application.model.merchant;

import com.kamatchibotique.application.model.common.Criteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantStoreCriteria extends Criteria {
	private boolean retailers = false;
	private boolean stores = false;
}
