package com.kamatchibotique.application.service.impl.tax;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;
import com.kamatchibotique.application.repository.tax.TaxClassRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.tax.TaxClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("taxClassService")
public class TaxClassServiceImpl extends CommonServiceImpl<Long, TaxClass>
		implements TaxClassService {

	private TaxClassRepository taxClassRepository;
	
	@Autowired
	public TaxClassServiceImpl(TaxClassRepository taxClassRepository) {
		super(taxClassRepository);
		
		this.taxClassRepository = taxClassRepository;
	}
	
	@Override
	public List<TaxClass> listByStore(MerchantStore store) throws ServiceException {	
		return taxClassRepository.findByStore(store.getId());
	}
	
	@Override
	public TaxClass getByCode(String code) throws ServiceException {
		return taxClassRepository.findByCode(code);
	}
	
	@Override
	public TaxClass getByCode(String code, MerchantStore store) throws ServiceException {
		return taxClassRepository.findByStoreAndCode(store.getId(), code);
	}
	
	@Override
	public void delete(TaxClass taxClass) throws ServiceException {
		
		TaxClass t = getById(taxClass.getId());
		super.delete(t);
		
	}
	
	@Override
	public TaxClass getById(Long id) {
		return taxClassRepository.getOne(id);
	}

	@Override
	public boolean exists(String code, MerchantStore store) throws ServiceException {
		Objects.requireNonNull(code, "TaxClass code cannot be empty");
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		
		return taxClassRepository.findByStoreAndCode(store.getId(), code) != null;

	}
	
	@Override
	public TaxClass saveOrUpdate(TaxClass taxClass) throws ServiceException {
		if(taxClass.getId()!=null && taxClass.getId() > 0) {
			this.update(taxClass);
		} else {
			taxClass = super.saveAndFlush(taxClass);
		}
		return taxClass;
	}

	

}
