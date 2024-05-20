package com.kamatchibotique.application.service.services.tax;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.reference.zone.Zone;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;
import com.kamatchibotique.application.model.tax.taxrate.TaxRate;

public interface TaxRateService extends CommonService<Long, TaxRate> {

	List<TaxRate> listByStore(MerchantStore store) throws ServiceException;

	List<TaxRate> listByCountryZoneAndTaxClass(Country country, Zone zone,
			TaxClass taxClass, MerchantStore store, Language language)
			throws ServiceException;

	List<TaxRate> listByCountryStateProvinceAndTaxClass(Country country,
			String stateProvince, TaxClass taxClass, MerchantStore store,
			Language language) throws ServiceException;

	 TaxRate getByCode(String code, MerchantStore store)
			throws ServiceException;
	 
	 TaxRate getById(Long id, MerchantStore store)
				throws ServiceException;

	List<TaxRate> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	TaxRate saveOrUpdate(TaxRate taxRate) throws ServiceException;
	
	

}
