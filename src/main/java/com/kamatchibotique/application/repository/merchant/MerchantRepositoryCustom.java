package com.kamatchibotique.application.repository.merchant;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.common.GenericEntityList;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.merchant.MerchantStoreCriteria;

public interface MerchantRepositoryCustom {

  GenericEntityList<MerchantStore> listByCriteria(MerchantStoreCriteria criteria)
      throws ServiceException;


}
