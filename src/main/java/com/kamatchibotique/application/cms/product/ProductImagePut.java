package com.kamatchibotique.application.cms.product;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.content.ImageContentFile;


public interface ProductImagePut {

  void addProductImage(ProductImage productImage, ImageContentFile contentImage)
      throws ServiceException;

}
