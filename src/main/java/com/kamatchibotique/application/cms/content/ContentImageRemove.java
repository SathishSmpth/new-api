package com.kamatchibotique.application.cms.content;


import com.kamatchibotique.application.cms.common.ImageRemove;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;

public interface ContentImageRemove extends ImageRemove {



  void removeImage(final String merchantStoreCode, final FileContentType imageContentType,
      final String imageName) throws ServiceException;

}
