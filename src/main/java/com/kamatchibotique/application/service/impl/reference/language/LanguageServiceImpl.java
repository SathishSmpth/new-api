package com.kamatchibotique.application.service.impl.reference.language;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.reference.language.LanguageRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.reference.language.LanguageService;
import com.kamatchibotique.application.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service("languageService")
public class LanguageServiceImpl extends CommonServiceImpl<Integer, Language>
	implements LanguageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LanguageServiceImpl.class);
	
	@Autowired
	private CacheUtils cache;
	
	private LanguageRepository languageRepository;
	
	@Autowired
	public LanguageServiceImpl(LanguageRepository languageRepository) {
		super(languageRepository);
		this.languageRepository = languageRepository;
	}
	
	
	@Override
	@Cacheable("languageByCode")
	public Language getByCode(String code) throws ServiceException {
		return languageRepository.findByCode(code);
	}
	
	@Override
	public Locale toLocale(Language language, MerchantStore store) {
		
		if(store != null) {
		
			String countryCode = store.getCountry().getIsoCode();
			
			return new Locale(language.getCode(), countryCode);
		
		} else {
			
			return new Locale(language.getCode());
		}
	}
	
	@Override
	public Language toLanguage(Locale locale) {
		Language language = null;
		try {
			language = getLanguagesMap().get(locale.getLanguage());
		} catch (Exception e) {
			LOGGER.error("Cannot convert locale " + locale.getLanguage() + " to language");
		}
		if(language == null) {
			language = new Language(Constants.DEFAULT_LANGUAGE);
		}
		return language;

	}
	
	@Override
	public Map<String,Language> getLanguagesMap() throws ServiceException {
		
		List<Language> langs = this.getLanguages();
		Map<String,Language> returnMap = new LinkedHashMap<String,Language>();
		
		for(Language lang : langs) {
			returnMap.put(lang.getCode(), lang);
		}
		return returnMap;

	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Language> getLanguages() throws ServiceException {
		

		List<Language> langs = null;
		try {

			langs = (List<Language>) cache.getFromCache("LANGUAGES");
			if(langs==null) {
				langs = this.list();

				
				cache.putInCache(langs, "LANGUAGES");
			}

		} catch (Exception e) {
			LOGGER.error("getCountries()", e);
			throw new ServiceException(e);
		}
		
		return langs;
		
	}
	
	@Override
	public Language defaultLanguage() {
		return toLanguage(Locale.ENGLISH);
	}

}
