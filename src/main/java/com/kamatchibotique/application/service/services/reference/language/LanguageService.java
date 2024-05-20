package com.kamatchibotique.application.service.services.reference.language;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface LanguageService extends CommonService<Integer, Language> {

	Language getByCode(String code) throws ServiceException;

	Map<String, Language> getLanguagesMap() throws ServiceException;

	List<Language> getLanguages() throws ServiceException;

	Locale toLocale(Language language, MerchantStore store);

	Language toLanguage(Locale locale);
	
	Language defaultLanguage();
}
