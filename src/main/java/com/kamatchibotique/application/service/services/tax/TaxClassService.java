package com.kamatchibotique.application.service.services.tax;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.tax.taxclass.TaxClass;

public interface TaxClassService extends CommonService<Long, TaxClass> {

	List<TaxClass> listByStore(MerchantStore store) throws ServiceException;

	TaxClass getByCode(String code) throws ServiceException;

	TaxClass getByCode(String code, MerchantStore store)
			throws ServiceException;
	
	boolean exists(String code, MerchantStore store) throws ServiceException;
	
	TaxClass saveOrUpdate(TaxClass taxClass) throws ServiceException;
}
