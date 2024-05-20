package com.kamatchibotique.application.service.impl.catalog.product.file;

import com.kamatchibotique.application.cms.content.StaticContentFileManager;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.file.DigitalProduct;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.repository.catalog.product.file.DigitalProductRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.ProductService;
import com.kamatchibotique.application.service.services.catalog.product.file.DigitalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service("digitalProductService")
public class DigitalProductServiceImpl extends CommonServiceImpl<Long, DigitalProduct>
        implements DigitalProductService {


    private DigitalProductRepository digitalProductRepository;

    @Autowired
    StaticContentFileManager productDownloadsFileManager;

    @Autowired
    ProductService productService;

    @Autowired
    public DigitalProductServiceImpl(DigitalProductRepository digitalProductRepository) {
        super(digitalProductRepository);
        this.digitalProductRepository = digitalProductRepository;
    }

    @Override
    public void addProductFile(Product product, DigitalProduct digitalProduct, InputContentFile inputFile) throws ServiceException {

        Assert.notNull(digitalProduct, "DigitalProduct cannot be null");
        Assert.notNull(product, "Product cannot be null");
        digitalProduct.setProduct(product);

        try {

            Assert.notNull(inputFile.getFile(), "InputContentFile.file cannot be null");

            Assert.notNull(product.getMerchantStore(), "Product.merchantStore cannot be null");
            this.saveOrUpdate(digitalProduct);

            String path = null;


            productDownloadsFileManager.addFile(product.getMerchantStore().getCode(), Optional.of(path), inputFile);

            product.setProductVirtual(true);
            productService.update(product);

        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            try {

                if (inputFile.getFile() != null) {
                    inputFile.getFile().close();
                }

            } catch (Exception ignore) {
            }
        }


    }

    @Override
    public DigitalProduct getByProduct(MerchantStore store, Product product) throws ServiceException {
        return digitalProductRepository.findByProduct(store.getId(), product.getId());
    }

    @Override
    public void delete(DigitalProduct digitalProduct) throws ServiceException {

        Assert.notNull(digitalProduct, "DigitalProduct cannot be null");
        Assert.notNull(digitalProduct.getProduct(), "DigitalProduct.product cannot be null");
        //refresh file
        digitalProduct = this.getById(digitalProduct.getId());
        super.delete(digitalProduct);

        String path = null;

        productDownloadsFileManager.removeFile(digitalProduct.getProduct().getMerchantStore().getCode(), FileContentType.PRODUCT, digitalProduct.getProductFileName(), Optional.of(path));
        digitalProduct.getProduct().setProductVirtual(false);
        productService.update(digitalProduct.getProduct());
    }


    @Override
    public void saveOrUpdate(DigitalProduct digitalProduct) throws ServiceException {

        Assert.notNull(digitalProduct, "DigitalProduct cannot be null");
        Assert.notNull(digitalProduct.getProduct(), "DigitalProduct.product cannot be null");
        if (digitalProduct.getId() == null || digitalProduct.getId() == 0) {
            super.save(digitalProduct);
        } else {
            super.create(digitalProduct);
        }

        digitalProduct.getProduct().setProductVirtual(true);
        productService.update(digitalProduct.getProduct());


    }


}
