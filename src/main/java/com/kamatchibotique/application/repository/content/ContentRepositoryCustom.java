package com.kamatchibotique.application.repository.content;

import java.util.List;

import com.kamatchibotique.application.model.content.ContentDescription;
import com.kamatchibotique.application.enums.content.ContentType;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


public interface ContentRepositoryCustom {

	List<ContentDescription> listNameByType(List<ContentType> contentType,
			MerchantStore store, Language language);

	ContentDescription getBySeUrl(MerchantStore store, String seUrl);
	

}
