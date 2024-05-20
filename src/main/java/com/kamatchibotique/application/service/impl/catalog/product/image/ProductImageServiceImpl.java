package com.kamatchibotique.application.service.impl.catalog.product.image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.kamatchibotique.application.configuration.events.products.DeleteProductImageEvent;
import com.kamatchibotique.application.configuration.events.products.SaveProductImageEvent;
import com.kamatchibotique.application.enums.FileContentType;

import com.kamatchibotique.application.enums.product.ProductImageSize;
import com.kamatchibotique.application.service.services.catalog.product.image.ProductImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.cms.product.ProductFileManager;
import com.kamatchibotique.application.repository.catalog.product.image.ProductImageRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.catalog.product.image.ProductImageDescription;
import com.kamatchibotique.application.model.content.ImageContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;

@Service("productImage")
public class ProductImageServiceImpl extends CommonServiceImpl<Long, ProductImage>
		implements ProductImageService {

	private ProductImageRepository productImageRepository;

	@Autowired
	public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
		super(productImageRepository);
		this.productImageRepository = productImageRepository;
	}

	@Autowired
	private ProductFileManager productFileManager;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public ProductImage getById(Long id) {

		return productImageRepository.findOne(id);
	}

	@Override
	public void addProductImages(Product product, List<ProductImage> productImages) throws ServiceException {

		try {
			for (ProductImage productImage : productImages) {

				Assert.notNull(productImage.getImage(), "Success");

				InputStream inputStream = productImage.getImage();
				ImageContentFile cmsContentImage = new ImageContentFile();
				cmsContentImage.setFileName(productImage.getProductImage());
				cmsContentImage.setFile(inputStream);
				cmsContentImage.setFileContentType(FileContentType.PRODUCT);

				addProductImage(product, productImage, cmsContentImage);
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void addProductImage(Product product, ProductImage productImage, ImageContentFile inputImage)
			throws ServiceException {

		productImage.setProduct(product);

		try {
			if (productImage.getImageType() == 0) {
				Assert.notNull(inputImage.getFile(), "ImageContentFile.file cannot be null");
				productFileManager.addProductImage(productImage, inputImage);
			}

			// insert ProductImage
			ProductImage img = saveOrUpdate(productImage);
			// manual workaround since aspect is not working
			eventPublisher.publishEvent(new SaveProductImageEvent(eventPublisher, img, product));

		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {

				if (inputImage.getFile() != null) {
					inputImage.getFile().close();
				}

			} catch (Exception ignore) {

			}
		}

	}

	@Override
	public ProductImage saveOrUpdate(ProductImage productImage) throws ServiceException {

		return productImageRepository.save(productImage);

	}

	public void addProductImageDescription(ProductImage productImage, ProductImageDescription description)
			throws ServiceException {

		if (productImage.getDescriptions() == null) {
			productImage.setDescriptions(new ArrayList<ProductImageDescription>());
		}

		productImage.getDescriptions().add(description);
		description.setProductImage(productImage);
		update(productImage);

	}

	@Override
	public OutputContentFile getProductImage(ProductImage productImage, ProductImageSize size) throws ServiceException {

		ProductImage pi = new ProductImage();
		String imageName = productImage.getProductImage();
		if (size == ProductImageSize.LARGE) {
			imageName = "L-" + imageName;
		}

		if (size == ProductImageSize.SMALL) {
			imageName = "S-" + imageName;
		}

		pi.setProductImage(imageName);
		pi.setProduct(productImage.getProduct());

		return productFileManager.getProductImage(pi);

	}

	@Override
	public OutputContentFile getProductImage(final String storeCode, final String productCode, final String fileName,
			final ProductImageSize size) throws ServiceException {
		return productFileManager.getProductImage(storeCode, productCode, fileName, size);

	}

	@Override
	public List<OutputContentFile> getProductImages(Product product) throws ServiceException {
		return productFileManager.getImages(product);
	}

	@Override
	public void removeProductImage(ProductImage productImage) throws ServiceException {

		if (!StringUtils.isBlank(productImage.getProductImage())) {
			productFileManager.removeProductImage(productImage);// managed internally
		}
		ProductImage p = getById(productImage.getId());

		Product product = p.getProduct();

		delete(p);
		/**
		 * workaround for aspect
		 */
		eventPublisher.publishEvent(new DeleteProductImageEvent(eventPublisher, p, product));

	}

	@Override
	public Optional<ProductImage> getProductImage(Long imageId, Long productId, MerchantStore store) {

		Optional<ProductImage> image = Optional.empty();

		ProductImage img = productImageRepository.finById(imageId, productId, store.getCode());
		if (img != null) {
			image = Optional.of(img);
		}

		return image;
	}

	@Override
	public void updateProductImage(Product product, ProductImage productImage) {
		Objects.requireNonNull(product, "Product cannot be null");
		Objects.requireNonNull(productImage, "ProductImage cannot be null");
		productImage.setProduct(product);
		productImageRepository.save(productImage);

	}
}
