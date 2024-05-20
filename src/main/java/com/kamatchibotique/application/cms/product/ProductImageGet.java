package com.kamatchibotique.application.cms.product;

import java.util.List;

import com.kamatchibotique.application.cms.common.ImageGet;
import com.kamatchibotique.application.enums.product.ProductImageSize;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.content.OutputContentFile;

public interface ProductImageGet extends ImageGet {
  OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
      final String imageName) throws ServiceException;

  OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
      final String imageName, final ProductImageSize size) throws ServiceException;

  OutputContentFile getProductImage(ProductImage productImage) throws ServiceException;

  List<OutputContentFile> getImages(Product product) throws ServiceException;


}
