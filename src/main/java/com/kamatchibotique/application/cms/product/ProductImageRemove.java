package com.kamatchibotique.application.cms.product;

import com.kamatchibotique.application.cms.common.ImageRemove;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;


public interface ProductImageRemove extends ImageRemove {

  void removeProductImage(ProductImage productImage) throws ServiceException;

  void removeProductImages(Product product) throws ServiceException;

}
