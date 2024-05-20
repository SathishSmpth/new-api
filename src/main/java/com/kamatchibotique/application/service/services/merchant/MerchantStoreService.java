package com.kamatchibotique.application.service.services.merchant;

import java.util.List;

import java.util.Optional;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.merchant.MerchantStoreCriteria;

public interface MerchantStoreService extends CommonService<Integer, MerchantStore>{
	

	MerchantStore getByCode(String code) throws ServiceException;
	
	MerchantStore getParent(String code) throws ServiceException;
	
	List<MerchantStore> findAllStoreNames() throws ServiceException;
	
	List<MerchantStore> findAllStoreNames(String code) throws ServiceException;

	List<MerchantStore> findAllStoreCodeNameEmail() throws ServiceException;

	Page<MerchantStore> listAll(Optional<String> storeName, int page, int count) throws ServiceException;
	
	Page<MerchantStore> listByGroup(Optional<String> storeName, String code, int page, int count) throws ServiceException;

	Page<MerchantStore> listAllRetailers(Optional<String> storeName, int page, int count) throws ServiceException;
	
	Page<MerchantStore> listChildren(String code, int page, int count) throws ServiceException;

	boolean existByCode(String code);
	
	/**
	 * Is parent or child and part of a specific group
	 * @param code
	 * @return
	 */
	boolean isStoreInGroup(String code) throws ServiceException;

	void saveOrUpdate(MerchantStore store) throws ServiceException;
	
	GenericEntityList<MerchantStore> getByCriteria(MerchantStoreCriteria criteria) throws ServiceException;

}
