package com.kamatchibotique.application.service.services.catalog.product.image;

import com.kamatchibotique.application.enums.product.ProductImageSize;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.content.ImageContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.service.services.common.CommonService;

import java.util.List;
import java.util.Optional;


public interface ProductImageService extends CommonService<Long, ProductImage> {


    void addProductImage(Product product, ProductImage productImage, ImageContentFile inputImage)
            throws ServiceException;

    OutputContentFile getProductImage(ProductImage productImage, ProductImageSize size)
            throws ServiceException;

    List<OutputContentFile> getProductImages(Product product)
            throws ServiceException;

    Optional<ProductImage> getProductImage(Long imageId, Long productId, MerchantStore store);

    void removeProductImage(ProductImage productImage) throws ServiceException;

    ProductImage saveOrUpdate(ProductImage productImage) throws ServiceException;

    OutputContentFile getProductImage(String storeCode, String productCode,
                                      String fileName, final ProductImageSize size) throws ServiceException;

    void addProductImages(Product product, List<ProductImage> productImages)
            throws ServiceException;

    void updateProductImage(Product product, ProductImage productImage);

}
