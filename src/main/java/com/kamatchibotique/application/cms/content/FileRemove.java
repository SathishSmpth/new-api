package com.kamatchibotique.application.cms.content;

import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;

import java.util.Optional;



public interface FileRemove {
  void removeFile(String merchantStoreCode, FileContentType staticContentType,
      String fileName, Optional<String> path) throws ServiceException;

  void removeFiles(String merchantStoreCode, Optional<String> path) throws ServiceException;

}
