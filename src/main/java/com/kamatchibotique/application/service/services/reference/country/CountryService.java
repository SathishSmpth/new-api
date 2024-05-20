package com.kamatchibotique.application.service.services.reference.country;

import java.util.List;
import java.util.Map;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.country.CountryDescription;
import com.kamatchibotique.application.model.reference.language.Language;

public interface CountryService extends CommonService<Integer, Country> {

	Country getByCode(String code) throws ServiceException;
	
	void addCountryDescription(Country country, CountryDescription description) throws ServiceException;

	List<Country> getCountries(Language language) throws ServiceException;

	Map<String, Country> getCountriesMap(Language language)
			throws ServiceException;

	List<Country> getCountries(List<String> isoCodes, Language language)
			throws ServiceException;
	List<Country> listCountryZones(Language language) throws ServiceException;
}
