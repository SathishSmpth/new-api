package com.kamatchibotique.application.service.impl.search;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.category.CategoryDescription;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionDescription;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionValueDescription;
import com.kamatchibotique.application.model.catalog.product.description.ProductDescription;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;
import com.kamatchibotique.application.model.catalog.product.inventory.ProductInventory;
import com.kamatchibotique.application.model.catalog.product.manufacturer.Manufacturer;
import com.kamatchibotique.application.model.catalog.product.manufacturer.ManufacturerDescription;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.service.services.catalog.inventory.ProductInventoryService;
import com.kamatchibotique.application.service.services.search.SearchService;
import com.kamatchibotique.application.utils.CoreConfiguration;
import jakarta.annotation.PostConstruct;
import modules.commons.search.SearchModule;
import modules.commons.search.configuration.SearchConfiguration;
import modules.commons.search.request.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service("productSearchService")
public class SearchServiceImpl implements SearchService {
	
	
    @Value("${search.noindex:false}")//skip indexing process
    private boolean noIndex;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	private final static String INDEX_PRODUCTS = "INDEX_PRODUCTS";
	
	private final static String SETTINGS = "search/SETTINGS";
	
	private final static String PRODUCT_MAPPING_DEFAULT = "search/MAPPINGS.json";
	
	private final static String QTY = "QTY";
	private final static String PRICE = "PRICE";
	private final static String DISCOUNT_PRICE = "DISCOUNT";
	private final static String SKU = "SKU";
	private final static String VSKU = "VSKU";
	
	
	/**
	 * TODO properties file
	 */
	
	private final static String KEYWORDS_MAPPING_DEFAULT = "{\"properties\":"
			+ "      {\n\"id\": {\n"
			+ "        \"type\": \"long\"\n"
			+ "      }\n"
			+ "     }\n"
			+ "    }";	
	


	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	private ProductInventoryService productInventoryService;

	@Autowired(required = false)
	private SearchModule searchModule;
	
	@Autowired
	private ResourceLoader resourceLoader;

	public void index(MerchantStore store, Product product) throws ServiceException {

		Objects.requireNonNull(product.getId(), "Product.id cannot be null");

		if (configuration.getProperty(INDEX_PRODUCTS) == null
				|| configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE) || searchModule == null) {
			return;
		}

		List<String> languages = languages(product);

		// existing documents
		List<Document> documents;
		List<Map<String, String>> variants = null;
		try {
			documents = document(product.getId(), languages, RequestOptions.DO_NOT_FAIL_ON_NOT_FOUND);

				if (!CollectionUtils.isEmpty(product.getVariants())) {
					variants = new ArrayList<Map<String, String>>();
					variants = product.getVariants().stream().map(i -> variants(i)).collect(Collectors.toList());
				}
	
				if (!CollectionUtils.isEmpty(documents)) {
					if (documents.iterator().next() != null) {
						searchModule.delete(languages, product.getId());
					}
				}


		} catch (Exception e) {
			throw new ServiceException(e);
		}

		Set<ProductDescription> descriptions = product.getDescriptions();

		for (ProductDescription description : descriptions) {
			indexProduct(store, description, product, variants);
		}

	}

	private List<Document> document(Long id, List<String> languages, RequestOptions options) throws Exception {
		List<Optional<Document>> documents = null;
		try {
			documents = searchModule.getDocument(id, languages, options);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Optional<Document> d : documents) {
			if(d == null) {//not allowed
				return Collections.emptyList();
			}
		}
		
		List<Document> filteredList = documents.stream().filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toList());

		return filteredList;

	}

	private void indexProduct(MerchantStore store, ProductDescription description, Product product,
			List<Map<String, String>> variants) throws ServiceException {

		try {
			ProductImage image = null;
			if (!CollectionUtils.isEmpty(product.getImages())) {
				image = product.getImages().stream().filter(i -> i.isDefaultImage()).findFirst()
						.orElse(product.getImages().iterator().next());
			}
			
			/**
			 * Inventory
			 */
			
			/**
			 * SKU, QTY, PRICE, DISCOUNT
			 */
			
			List<Map<String, String>> itemInventory = new ArrayList<Map<String, String>>();
			
			itemInventory.add(inventory(product));
			
			if (!CollectionUtils.isEmpty(product.getVariants())) {
				for(ProductVariant variant : product.getVariants()) {
					itemInventory.add(inventory(variant));
				}
			}

			IndexItem item = new IndexItem();
			item.setId(product.getId());
			item.setStore(store.getCode().toLowerCase());
			item.setDescription(description.getDescription());
			item.setName(description.getName());
			item.setInventory(itemInventory);


			if (product.getManufacturer() != null) {
				item.setBrand(manufacturer(product.getManufacturer(), description.getLanguage().getCode()));
			}

			if (!CollectionUtils.isEmpty(product.getCategories())) {
				item.setCategory(
						category(product.getCategories().iterator().next(), description.getLanguage().getCode()));
			}

			if (!CollectionUtils.isEmpty(product.getAttributes())) {
				Map<String, String> attributes = attributes(product, description.getLanguage().getCode());
				item.setAttributes(attributes);
			}

			if (image != null) {
				item.setImage(image.getProductImage());
			}

			if (product.getProductReviewAvg() != null) {
				item.setReviews(product.getProductReviewAvg().toString());
			}

			if (!CollectionUtils.isEmpty(variants)) {
				item.setVariants(variants);
			}

			item.setLanguage(description.getLanguage().getCode());
			item.setLink(description.getSeUrl());

			searchModule.index(item);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
	
	private Map<String, String> inventory(Product product) throws Exception {
		
		
		/**
		 * Default inventory
		 */
		
		ProductInventory inventory = productInventoryService.inventory(product);
		
		Map<String, String> inventoryMap = new HashMap<String, String>();
		inventoryMap.put(SKU, product.getSku());
		inventoryMap.put(QTY, String.valueOf(inventory.getQuantity()));
		inventoryMap.put(PRICE, String.valueOf(inventory.getPrice().getStringPrice()));
		if(inventory.getPrice().isDiscounted()) {
			inventoryMap.put(DISCOUNT_PRICE, String.valueOf(inventory.getPrice().getStringDiscountedPrice()));
		}
		
		
		return inventoryMap;
	}
	
	private Map<String, String> inventory(ProductVariant product) throws Exception {
		
		
		/**
		 * Default inventory
		 */
		
		ProductInventory inventory = productInventoryService.inventory(product);
		
		Map<String, String> inventoryMap = new HashMap<String, String>();
		inventoryMap.put(SKU, product.getSku());
		inventoryMap.put(QTY, String.valueOf(inventory.getQuantity()));
		inventoryMap.put(PRICE, String.valueOf(inventory.getPrice().getStringPrice()));
		if(inventory.getPrice().isDiscounted()) {
			inventoryMap.put(DISCOUNT_PRICE, String.valueOf(inventory.getPrice().getStringDiscountedPrice()));
		}
		
		
		return inventoryMap;
	}

	private Map<String, String> variants(ProductVariant variant) {
		if (variant == null)
			return null;

		Map<String, String> variantMap = new HashMap<String, String>();
		if (variant.getVariation() != null) {
			String variantCode = variant.getVariation().getProductOption().getCode();
			String variantValueCode = variant.getVariation().getProductOptionValue().getCode();

			variantMap.put(variantCode, variantValueCode);

		}
		

		if (variant.getVariationValue() != null) {
			String variantCode = variant.getVariationValue().getProductOption().getCode();
			String variantValueCode = variant.getVariationValue().getProductOptionValue().getCode();
			variantMap.put(variantCode, variantValueCode);
		}
		
		if(!StringUtils.isBlank(variant.getSku())) {
			variantMap.put(VSKU, variant.getSku());
		}
		


		return variantMap;

	}

	@Override
	public void deleteDocument(MerchantStore store, Product product) throws ServiceException {

		if (configuration.getProperty(INDEX_PRODUCTS) == null
				|| configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE) || searchModule == null) {
			return;
		}

		List<String> languages = languages(product);

		try {
			searchModule.delete(languages, product.getId());
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public SearchResponse searchKeywords(MerchantStore store, String language, SearchRequest search, int entriesCount)
			throws ServiceException {

		if (configuration.getProperty(INDEX_PRODUCTS) == null
				|| configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE) || searchModule == null) {
			return null;
		}

		try {
			return searchModule.searchKeywords(search);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SearchResponse search(MerchantStore store, String language, SearchRequest search, int entriesCount,
			int startIndex) throws ServiceException {

		if (configuration.getProperty(INDEX_PRODUCTS) == null
				|| configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE) || searchModule == null) {
			return null;
		}

		try {
			return searchModule.searchProducts(search);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public Optional<Document> getDocument(String language, MerchantStore store, Long productId)
			throws ServiceException {
		if (configuration.getProperty(INDEX_PRODUCTS) == null
				|| configuration.getProperty(INDEX_PRODUCTS).equals(Constants.FALSE) || searchModule == null) {
			return null;
		}

		try {
			return searchModule.getDocument(productId, language, RequestOptions.DEFAULT);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private List<String> languages(Product product) {
		return product.getDescriptions().stream().map(d -> d.getLanguage().getCode()).collect(Collectors.toList());
	}

	private String manufacturer(Manufacturer manufacturer, String language) {
		ManufacturerDescription description = manufacturer.getDescriptions().stream()
				.filter(d -> d.getLanguage().getCode().equals(language)).findFirst().get();
		return description.getName();
	}

	private String category(Category category, String language) {
		CategoryDescription description = category.getDescriptions().stream()
				.filter(d -> d.getLanguage().getCode().equals(language)).findFirst().get();
		return description.getName();
	}

	private Map<String, String> attributes(Product product, String language) {

		/**
		 * ProductAttribute ProductOption ProductOptionValue
		 */

		Map<String, String> allAttributes = new HashMap<String, String>();

		for (ProductAttribute attribute : product.getAttributes()) {
			Map<String, String> attr = attribute(attribute, language);
			allAttributes.putAll(attr);
		}

		return allAttributes;

	}

	private Map<String, String> attribute(ProductAttribute attribute, String language) {
		Map<String, String> attributeValue = new HashMap<String, String>();

		ProductOptionDescription optionDescription = attribute.getProductOption().getDescriptions().stream()
				.filter(a -> a.getLanguage().getCode().equals(language)).findFirst().get();		
				
		ProductOptionValueDescription value = attribute.getProductOptionValue().getDescriptions().stream()
				.filter(a -> a.getLanguage().getCode().equals(language)).findFirst().get();

		attributeValue.put(optionDescription.getName(), value.getName());

		return attributeValue;
	}
	
	private void settings(SearchConfiguration config, String language) throws Exception{
		Objects.requireNonNull(language, "Configuration requires language");
		String settings = resourceAsText(loadSearchConfig(SETTINGS + "_DEFAULT.json"));
		//specific settings
		if(language.equals("en")) {
			settings = resourceAsText(loadSearchConfig(SETTINGS+ "_" + language +".json"));
		}
		
		config.getSettings().put(language, settings);

	}
	
	private void mappings(SearchConfiguration config, String language) throws Exception {
		Objects.requireNonNull(language, "Configuration requires language");

		config.getProductMappings().put(language, resourceAsText(loadSearchConfig(PRODUCT_MAPPING_DEFAULT)));
		config.getKeywordsMappings().put(language,KEYWORDS_MAPPING_DEFAULT);
			
	}

	
	private String resourceAsText(Resource resource) throws Exception {
		InputStream mappingstream = resource.getInputStream();
		
	    return new BufferedReader(
	    	      new InputStreamReader(mappingstream, StandardCharsets.UTF_8))
	    	        .lines()
	    	        .collect(Collectors.joining("\n"));
	}
	
	private Resource loadSearchConfig(String file) {
	    return resourceLoader.getResource(
	      "classpath:" + file);
	}

}
