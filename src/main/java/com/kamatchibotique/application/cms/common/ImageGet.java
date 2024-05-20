package com.kamatchibotique.application.cms.common;

import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.OutputContentFile;

import java.util.List;

public interface ImageGet {

  List<OutputContentFile> getImages(final String merchantStoreCode,
      FileContentType imageContentType) throws ServiceException;

}
