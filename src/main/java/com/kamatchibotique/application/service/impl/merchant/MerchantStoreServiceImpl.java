package com.kamatchibotique.application.service.impl.merchant;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.merchant.MerchantStoreCriteria;
import com.kamatchibotique.application.repository.merchant.MerchantRepository;
import com.kamatchibotique.application.repository.merchant.PageableMerchantRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.type.ProductTypeService;
import com.kamatchibotique.application.service.services.merchant.MerchantStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("merchantService")
public class MerchantStoreServiceImpl extends CommonServiceImpl<Integer, MerchantStore>
		implements MerchantStoreService {

	@Autowired
	protected ProductTypeService productTypeService;

	@Autowired
	private PageableMerchantRepository pageableMerchantRepository;

	private MerchantRepository merchantRepository;

	@Autowired
	public MerchantStoreServiceImpl(MerchantRepository merchantRepository) {
		super(merchantRepository);
		this.merchantRepository = merchantRepository;
	}

	@Override
	//@CacheEvict(value="store", key="#store.code")
	public void saveOrUpdate(MerchantStore store) throws ServiceException {
		super.save(store);
	}

	@Override
	/**
	 * cache moved in facades
	 */
	//@Cacheable(value = "store")
	public MerchantStore getByCode(String code) throws ServiceException {
		return merchantRepository.findByCode(code);
	}

	@Override
	public boolean existByCode(String code) {
		return merchantRepository.existsByCode(code);
	}

	@Override
	public GenericEntityList<MerchantStore> getByCriteria(MerchantStoreCriteria criteria) throws ServiceException {
		return merchantRepository.listByCriteria(criteria);
	}

	@Override
	public Page<MerchantStore> listChildren(String code, int page, int count) throws ServiceException {
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableMerchantRepository.listByStore(code, pageRequest);
	}

	@Override
	public Page<MerchantStore> listAll(Optional<String> storeName, int page, int count) throws ServiceException {
		String store = null;
		if (storeName != null && storeName.isPresent()) {
			store = storeName.get();
		}
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableMerchantRepository.listAll(store, pageRequest);

	}

	@Override
	public List<MerchantStore> findAllStoreCodeNameEmail() throws ServiceException {
		return merchantRepository.findAllStoreCodeNameEmail();
	}

	@Override
	public Page<MerchantStore> listAllRetailers(Optional<String> storeName, int page, int count)
			throws ServiceException {
		String store = null;
		if (storeName != null && storeName.isPresent()) {
			store = storeName.get();
		}
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableMerchantRepository.listAllRetailers(store, pageRequest);

	}

	@Override
	public List<MerchantStore> findAllStoreNames() throws ServiceException {
		return merchantRepository.findAllStoreNames();
	}

	@Override
	public MerchantStore getParent(String code) throws ServiceException {
		Objects.requireNonNull(code, "MerchantStore code cannot be null");

		
		//get it
		MerchantStore storeModel = this.getByCode(code);
		
		if(storeModel == null) {
			throw new ServiceException("Store with code [" + code + "] is not found");
		}
		
		if(storeModel.getRetailer() != null && storeModel.getRetailer() && storeModel.getParent() == null) {
			return storeModel;
		}
		
		if(storeModel.getParent() == null) {
			return storeModel;
		}
	
		return merchantRepository.getById(storeModel.getParent().getId());
	}


	@Override
	public List<MerchantStore> findAllStoreNames(String code) throws ServiceException {
		return merchantRepository.findAllStoreNames(code);
	}

	/**
	 * Store might be alone (known as retailer)
	 * A retailer can have multiple child attached
	 * 
	 * This method from a store code is able to retrieve parent and childs.
	 * Method can also filter on storeName
	 */
	@Override
	public Page<MerchantStore> listByGroup(Optional<String> storeName, String code, int page, int count) throws ServiceException {
		
		String name = null;
		if (storeName != null && storeName.isPresent()) {
			name = storeName.get();
		}

		
		MerchantStore store = getByCode(code);//if exist
		Optional<Integer> id = Optional.ofNullable(store.getId());

		
		Pageable pageRequest = PageRequest.of(page, count);


		return pageableMerchantRepository.listByGroup(code, id.get(), name, pageRequest);
		
		
	}

	@Override
	public boolean isStoreInGroup(String code) throws ServiceException{
		
		MerchantStore store = getByCode(code);//if exist
		Optional<Integer> id = Optional.ofNullable(store.getId());
		
		List<MerchantStore> stores = merchantRepository.listByGroup(code, id.get());
		
		
		return stores.size() > 0;
	}


}
