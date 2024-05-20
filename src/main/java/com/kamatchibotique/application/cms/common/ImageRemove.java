package com.kamatchibotique.application.cms.common;

import com.kamatchibotique.application.exception.ServiceException;


public interface ImageRemove {

  void removeImages(final String merchantStoreCode) throws ServiceException;

}
