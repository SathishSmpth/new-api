package com.kamatchibotique.application.cms.content;

import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.OutputContentFile;


public interface FileGet {

  OutputContentFile getFile(final String merchantStoreCode, Optional<String> path, FileContentType fileContentType,
      String contentName) throws ServiceException;

  List<String> getFileNames(final String merchantStoreCode, Optional<String> path, FileContentType fileContentType)
      throws ServiceException;

  List<OutputContentFile> getFiles(final String merchantStoreCode,
		  Optional<String> path, FileContentType fileContentType) throws ServiceException;
}
