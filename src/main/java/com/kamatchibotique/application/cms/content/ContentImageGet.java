package com.kamatchibotique.application.cms.content;

import java.util.List;

import com.kamatchibotique.application.cms.common.ImageGet;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.OutputContentFile;

public interface ContentImageGet extends ImageGet {

  OutputContentFile getImage(final String merchantStoreCode, String imageName,
      FileContentType imageContentType) throws ServiceException;

  List<String> getImageNames(final String merchantStoreCode,
      FileContentType imageContentType) throws ServiceException;

}
