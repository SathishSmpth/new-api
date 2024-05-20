package com.kamatchibotique.application.cms.content;

import java.util.List;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.InputContentFile;

public interface ImagePut {


  void addImage(final String merchantStoreCode, InputContentFile image)
      throws ServiceException;

  void addImages(final String merchantStoreCode, List<InputContentFile> imagesList)
      throws ServiceException;

}
