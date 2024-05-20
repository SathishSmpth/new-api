package com.kamatchibotique.application.service.impl.tax;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.reference.zone.Zone;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;
import com.kamatchibotique.application.model.tax.taxrate.TaxRate;
import com.kamatchibotique.application.repository.tax.TaxRateRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.tax.TaxRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taxRateService")
public class TaxRateServiceImpl extends CommonServiceImpl<Long, TaxRate>
		implements TaxRateService {

	private TaxRateRepository taxRateRepository;
	
	@Autowired
	public TaxRateServiceImpl(TaxRateRepository taxRateRepository) {
		super(taxRateRepository);
		this.taxRateRepository = taxRateRepository;
	}

	@Override
	public List<TaxRate> listByStore(MerchantStore store)
			throws ServiceException {
		return taxRateRepository.findByStore(store.getId());
	}
	
	@Override
	public List<TaxRate> listByStore(MerchantStore store, Language language)
			throws ServiceException {
		return taxRateRepository.findByStoreAndLanguage(store.getId(), language.getId());
	}
	
	
	@Override
	public TaxRate getByCode(String code, MerchantStore store)
			throws ServiceException {
		return taxRateRepository.findByStoreAndCode(store.getId(), code);
	}
	
	@Override
	public List<TaxRate> listByCountryZoneAndTaxClass(Country country, Zone zone, TaxClass taxClass, MerchantStore store, Language language) throws ServiceException {
		return taxRateRepository.findByMerchantAndZoneAndCountryAndLanguage(store.getId(), zone.getId(), country.getId(), language.getId());
	}
	
	@Override
	public List<TaxRate> listByCountryStateProvinceAndTaxClass(Country country, String stateProvince, TaxClass taxClass, MerchantStore store, Language language) throws ServiceException {
		return taxRateRepository.findByMerchantAndProvinceAndCountryAndLanguage(store.getId(), stateProvince, country.getId(), language.getId());
	}
	
	@Override
	public void delete(TaxRate taxRate) throws ServiceException {
		
		taxRateRepository.delete(taxRate);
		
	}
	
	@Override
	public TaxRate saveOrUpdate(TaxRate taxRate) throws ServiceException {
		if(taxRate.getId()!=null && taxRate.getId() > 0) {
			this.update(taxRate);
		} else {
			taxRate = super.saveAndFlush(taxRate);
		}
		return taxRate;
	}

	@Override
	public TaxRate getById(Long id, MerchantStore store) throws ServiceException {
		return taxRateRepository.findByStoreAndId(store.getId(), id);
	}
		

	
}
